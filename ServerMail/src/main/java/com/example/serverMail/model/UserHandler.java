package com.example.serverMail.model;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UserHandler {
    JsonArray mailboxes;
    String fileName = "mailboxes.json";
    private final String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
    private final Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
    public UserHandler() {
        mailboxes = new JsonArray();
    }
    public boolean addUser(MailBox mailBox) throws IOException {
        if (!isEmailValid(mailBox.getMailBoxOwner())) {
            return false;
        }
        try (FileReader fileReader = new FileReader(fileName)) { // read the existing file
            // parse the existing JSON data into a JsonArray
            JsonParser parser = new JsonParser();
            mailboxes = parser.parse(fileReader).getAsJsonArray();
        } catch (FileNotFoundException e) {
            // if the file does not exist, create a new empty array
            mailboxes = new JsonArray();
        } catch (IOException e) {
            System.out.println("There is a problem with writing the mailbox" + e);
        }
        try (FileWriter fileWriter = new FileWriter(fileName)) { // overwrite the file with the updated data
            // create a JsonObject for each mailbox
            JsonObject mailbox1 = new JsonObject();
            mailbox1.add("mailBoxOwner", new JsonPrimitive(mailBox.getMailBoxOwner()));
            mailbox1.add("rEmails", new JsonArray());
            mailbox1.add("sEmails", new JsonArray());
            // add the mailbox to the array
            mailboxes.add(mailbox1);
            Gson gson = new GsonBuilder().create();
            // write the array to the file
            String json = gson.toJson(mailboxes); // use the mailboxes array instead of the mailBox parameter
            fileWriter.write(json + System.lineSeparator());
            return true;
        } catch (IOException e) {
            System.out.println("There is a problem with writing the mailbox" + e);
        }
        return false;
    }
    //when receiving an email it should add Email inside rEmails[]
    public boolean addEmailToMailBox(String mailBoxOwner, Email email) {
        try (FileReader fileReader = new FileReader(fileName)) {
            JsonParser parser = new JsonParser();
            mailboxes = parser.parse(fileReader).getAsJsonArray();
        } catch (FileNotFoundException e) {
            System.out.println("Mailbox file not found.");
            return false;
        } catch (IOException e) {
            System.out.println("There is a problem with reading the mailbox" + e);
            return false;
        }

        for (JsonElement element : mailboxes) {
            JsonObject mailboxJson = element.getAsJsonObject();
            String currentMailBoxOwner = mailboxJson.getAsJsonPrimitive("mailBoxOwner").getAsString();

            if (currentMailBoxOwner.equals(mailBoxOwner)) {
                // Found the mailbox, add the email
                JsonObject emailJson = new JsonObject();
                emailJson.addProperty("sender", email.getSender());
                // Convert recipients list to JSON array
                JsonArray recipientsArray = new JsonArray();
                for (String recipient : email.getRecipients()) {
                    recipientsArray.add(new JsonPrimitive(recipient));
                }
                emailJson.add("recipients", recipientsArray);

                emailJson.addProperty("subject", email.getSubject());
                emailJson.addProperty("body", email.getBody());
                emailJson.addProperty("time", email.getTime().toString());
                emailJson.addProperty("ID", email.getID());

                mailboxJson.getAsJsonArray("rEmails").add(emailJson);

                try (FileWriter fileWriter = new FileWriter(fileName)) {
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(mailboxes);
                    fileWriter.write(json + System.lineSeparator());
                    return true;
                } catch (IOException e) {
                    System.out.println("There is a problem with writing the mailbox" + e);
                    return false;
                }
            }
        }

        // If the mailbox doesn't exist
        System.out.println("Mailbox not found.");
        return false;
    }


    private boolean isEmailValid(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public List<MailBox> readAllMailBoxes() {
        List<MailBox> mailBoxes = new ArrayList<>();

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                // Create the file if it doesn't exist
                file.createNewFile();
            }

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                Gson gson = new GsonBuilder().create();
                while ((line = bufferedReader.readLine()) != null) {
                    // parse the line into a list of mailboxes
                    Type listType = new TypeToken<List<MailBox>>() {}.getType();
                    mailBoxes = gson.fromJson(line, listType);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in UserHandler reading mailboxes " + e);
        }

        return mailBoxes;
    }


    public boolean verifyUser(String userName) {
        List<MailBox> mailBoxes = readAllMailBoxes();
        for (MailBox mailBox : mailBoxes) {
            System.out.println("Checking " + userName + " current mailBoxOwner " + mailBox.getMailBoxOwner());
            if (mailBox.getMailBoxOwner().equals(userName)) {
                return true;
            }
        }
        return false;
    }
}
