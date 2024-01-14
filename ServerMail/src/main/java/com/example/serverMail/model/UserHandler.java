package com.example.serverMail.model;

import com.google.gson.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHandler {
    private final Map<String, ReadWriteLock> mailboxLocks = new HashMap<>();
    private final ReadWriteLock usernameLock = new ReentrantReadWriteLock();
    String fileName = "usernames.txt";
    private final String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
    private final Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

    private final HashMap<String, Date> lastRefreshTimes = new HashMap<>();
    private final HashMap<String, Integer> lastIdSent = new HashMap<>();
    private final HashMap<String, Integer> lastIdReceive = new HashMap<>();

    public UserHandler() {
        try {
            List<String> users = readUsers();
            for (String user : users) {
                mailboxLocks.put(user, new ReentrantReadWriteLock());
            }
            initializeLastRefreshTimes();
        } catch (IOException e) {
            System.out.println("There was an error" + e);
        }
    }

    // Given a mailbox it writes it to username@email.com.json, so it can be used
    // for updating existing mailbox
    public boolean writeMailbox(MailBox mailBox) {
        String user = mailBox.getMailBoxOwner();
        if (!isEmailValid(user)) {
            return false;
        }

        // If it's a new user then add it to the user list
        if (!checkUserExists(user)) {
            Lock writeLock = usernameLock.writeLock();
            try {
                writeLock.lock();
                try (FileWriter usernamesWriter = new FileWriter(fileName, true)) {
                    usernamesWriter.write(mailBox.getMailBoxOwner() + System.lineSeparator());
                    mailboxLocks.put(user, new ReentrantReadWriteLock());
                    lastRefreshTimes.put(user, new Date(0));
                    lastIdSent.put(user, -1);
                    lastIdReceive.put(user,-1);
                } catch (IOException e) {
                    System.out.println("There is a problem while writing to usernames.txt " + e);
                    return false;
                }
            } finally {
                writeLock.unlock();
            }
        }

        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + user + ".json";

        // Create the directory if it doesn't exist
        File directory = new File(folderPath);
        if (!directory.exists()) {
            if (!directory.mkdirs())
                return false;
        }
        Lock wl = mailboxLocks.get(user).writeLock();
        try (FileWriter fileWriter = new FileWriter(userFileName)) {
            wl.lock();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(mailBox);
            fileWriter.write(json);
            return true;
        } catch (IOException e) {
            System.out.println("There is a problem while writing a mailbox " + e);
        } finally {
            wl.unlock();
        }
        return false;
    }

    public MailBox readUserMailbox(String username) {
        if (!checkUserExists(username)) {
            return null; // Indicate that the user doesn't exist
        }

        Lock rl = mailboxLocks.get(username).readLock();

        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + username + ".json";

        try {
            rl.lock();
            try (FileReader fileReader = new FileReader(userFileName)) {
                return new Gson().fromJson(fileReader, MailBox.class);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("The user file is unexpectedly missing: " + e.getMessage(), e);
            } catch (IOException e) {
                throw new RuntimeException("Error reading user mailbox: " + e.getMessage(), e);
            }
        } finally {
            rl.unlock();
        }
    }

    // deletes Email from both mailbox file and ArrayList of Emails for given user
    public boolean deleteEmailFromMailbox(String username, Email emailToDel, Boolean inbox) {
        int id = emailToDel.getID();
        MailBox mailBox = readUserMailbox(username);
        ArrayList<Email> emails = inbox ? mailBox.getrEmails() : mailBox.getsEmails();
        for (Email e : emails) {
            if (e.getID() == id) {
                emails.remove(e);
                writeMailbox(mailBox);
                return true;
            }
        }
        return false;
    }

    // reads user list from usernames.txt then returns List of users
    public List<String> readUsers() {
        List<String> usernames = new ArrayList<>();
        Lock readLock = usernameLock.readLock();
        try {
            readLock.lock();
            File file = new File(fileName);
            if (file.exists()) {
                try (FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        usernames.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in UserHandler reading usernames " + e);
        } finally {
            readLock.unlock();
        }
        return usernames;
    }

    // checks if email format is valid by using pattern matcher
    private boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    // checks if the user exists or not by reading usernames.txt file
    public boolean checkUserExists(String userName) {
        List<String> usernames = readUsers();
        System.out.println(usernames);
        for (String user : usernames) {
            if (user.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    // Initializes the lastRefreshTimes HashMap by setting the last refresh time of
    // all users to the epoch time (January 1, 1970, 00:00:00 GMT). It does this by
    // creating a new Date object with the argument 0
    private void initializeLastRefreshTimes() throws IOException {
        List<String> allUsernames = readUsers();
        Date initialDate = new Date(0);
        for (String username : allUsernames) {
            lastRefreshTimes.put(username, initialDate);
            lastIdReceive.put(username,-1);
            lastIdSent.put(username,-1);
        }
    }

    // Returns the last refresh time for a given username. If the username does not
    // exist in the lastRefreshTimes HashMap, it returns a new Date object set to
    // the epoch time
    public Date getLastRefreshTime(String username) {
        return lastRefreshTimes.getOrDefault(username, new Date(0));
    }

    // Returns a list of received emails for a given username that were received
    // after the last refresh time. It does this by iterating over the user's
    // mailbox and adding any emails with a time after the last refresh time to the
    // newEmails list.
    public ArrayList<Email> getNewREmails(String username) {
        ArrayList<Email> newEmails = new ArrayList<>();
        MailBox mailbox = readUserMailbox(username);
        if(!mailbox.getrEmails().isEmpty()) {
            Integer newLastIdReceived = mailbox.getrEmails().getFirst().getID();
            for (Email email : mailbox.getrEmails()) {
                if (email.getID() > getLastIdReceive(username)) {
                    newEmails.add(email);
                }
            }
            updateLastIdReceive(username, newLastIdReceived);
        }
        return newEmails;
    }

    public Integer getLastIdSent(String username) {
        return lastIdSent.get(username);
    }

    public Integer getLastIdReceive(String username) {
        return lastIdReceive.get(username);
    }

    // Returns a list of sent emails for a given username that were sent after the
    // last refresh time.
    public ArrayList<Email> getNewSEmails(String username) {
        ArrayList<Email> newEmails = new ArrayList<>();
        MailBox mailbox = readUserMailbox(username);
        if(!mailbox.getsEmails().isEmpty()) {
        Integer newLastIdSent = mailbox.getsEmails().getFirst().getID();
        for (Email email : mailbox.getsEmails()) {
            if (email.getID() > getLastIdSent(username) ) {
                newEmails.add(email);
            }
        }
        updateLastIdSent(username,newLastIdSent);
        }
        return newEmails;
    }

    public void updateLastIdSent(String username, Integer id){
        lastIdSent.put(username,id);
    }
    public void updateLastIdReceive(String username, Integer id){
        lastIdReceive.put(username,id);
    }

    public void resetIdSent(String username){
        lastIdSent.put(username, -1);
    }
    public void resetIdReceive(String username){
        lastIdReceive.put(username, -1);
    }

    // Updates the last refresh time for a given username to the current time. It
    // does this by creating a new Date object, which is initialized to the current
    // time.
    public void updateLastRefreshTime(String username, Date lastUpdate) {
        lastRefreshTimes.put(username, lastUpdate);

    }

    // Resets the last refresh time for a given username to the epoch time.
    public void resetRefreshTime(String username) {
        lastRefreshTimes.put(username, new Date(0));
    }
}
