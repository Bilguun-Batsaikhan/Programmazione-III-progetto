package com.example.serverMail.model;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UserHandler {
    String fileName = "usernames.txt";
    private final String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
    private final Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
    public UserHandler() {}

    //Given a mailbox it writes it to username@email.com.json, so it can be used for updating existing mailbox
    public boolean writeMailbox(MailBox mailBox) {
        //If it's a new user then add it to the user list
        if(!checkUserExists(mailBox.getMailBoxOwner())) {
            try (FileWriter usernamesWriter = new FileWriter(fileName, true)) {
                usernamesWriter.write(mailBox.getMailBoxOwner() + System.lineSeparator());
            } catch (IOException e) {
                System.out.println("There is a problem while writing to usernames.txt " + e);
            }
        }
        if (!isEmailValid(mailBox.getMailBoxOwner())) {
            return false;
        }
        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + mailBox.getMailBoxOwner() + ".json";
        try (FileWriter fileWriter = new FileWriter(userFileName)) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(mailBox);
            fileWriter.write(json);
            return true;
        } catch (IOException e) {
            System.out.println("There is a problem while writing a mailbox " + e);
        }
        return false;
    }


    public MailBox readUserMailbox(String username) {
        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + username + ".json";
        try(FileReader fileReader = new FileReader(userFileName)) {
            return new Gson().fromJson(fileReader, MailBox.class);
        } catch (FileNotFoundException e) {
            System.out.println("The user doesn't exist " + e.getMessage());
        } catch (IOException e) {
            System.out.println("There is a problem while reading user " + e.getMessage());
        }
        // null for indicating that user doesn't exist
        return null;
    }

    public boolean deleteEmailFromMailbox(String username, Email emailToDel, Boolean inbox) {
        String id = emailToDel.getID();
        MailBox mailBox = readUserMailbox(username);
        ArrayList<Email> emails = inbox ? mailBox.getrEmails() : mailBox.getsEmails();
        for(Email e : emails) {
            if(e.getID().equals(id)) {
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
        try {
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
        }
        return usernames;
    }
    private boolean isEmailValid(String email) {
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
