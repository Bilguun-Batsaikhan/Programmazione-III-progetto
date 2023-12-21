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

    public void writeMailbox(MailBox mailBox) {
        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + mailBox.getMailBoxOwner() + ".json";
        try (FileWriter fileWriter = new FileWriter(userFileName)) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(mailBox);
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(MailBox mailBox) throws IOException {
        if (!isEmailValid(mailBox.getMailBoxOwner())) {
            return false;
        }

        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + mailBox.getMailBoxOwner() + ".json";

        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        folder.mkdirs();

        try (FileWriter fileWriter = new FileWriter(userFileName)) {
            // Create a JsonObject for the mailbox
            JsonObject mailbox = new JsonObject();
            mailbox.add("mailBoxOwner", new JsonPrimitive(mailBox.getMailBoxOwner()));
            mailbox.add("rEmails", new JsonArray());
            mailbox.add("sEmails", new JsonArray());

            Gson gson = new GsonBuilder().create();
            // Write the mailbox to the file
            String json = gson.toJson(mailbox);
            fileWriter.write(json);

            try (FileWriter usernamesWriter = new FileWriter(fileName, true)) {
                usernamesWriter.write(mailBox.getMailBoxOwner() + System.lineSeparator());
            }
            return true;
        } catch (IOException e) {
            System.out.println("There is a problem with writing the mailbox: " + e);
        }
        return false;
    }
    public MailBox readUserMailbox(String userFile) {
        String folderPath = "user_files";
        String userFileName = folderPath + File.separator + userFile + ".json";
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
        if(email == null || email.isEmpty()){
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }
    public boolean verifyUser(String userName) {
        List<String> usernames = readUsers();
        for (String user : usernames) {
            if (user.equals(userName)) {
                return true;
            }
        }
        return false;
    }
}
