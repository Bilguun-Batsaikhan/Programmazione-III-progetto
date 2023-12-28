package com.example.serverMail.model;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public UserHandler() {
        List<String> users = readUsers();
        for(String user: users) {
            mailboxLocks.put(user, new ReentrantReadWriteLock());
        }
    }

    //Given a mailbox it writes it to username@email.com.json, so it can be used for updating existing mailbox
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
                if(!directory.mkdirs()) return false;
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


    public boolean deleteEmailFromMailbox(String username, Email emailToDel, Boolean inbox) {
            int id = emailToDel.getID();
            MailBox mailBox = readUserMailbox(username);
            ArrayList<Email> emails = inbox ? mailBox.getrEmails() : mailBox.getsEmails();
            for(Email e : emails) {
                if(e.getID() == id) {
                    emails.remove(e);
                    writeMailbox(mailBox);
                    return true;
                }
            }
            return false;
        }

        //reads user list from usernames.txt then returns List of users
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
        private boolean isEmailValid(String email) {
            if(email == null || email.isEmpty()){
                return false;
            }
            Matcher matcher = pattern.matcher(email);
            return matcher.find();
        }
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
}
