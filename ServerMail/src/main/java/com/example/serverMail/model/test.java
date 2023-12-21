package com.example.serverMail.model;

import java.util.Date;
import java.util.List;

public class test {
    public static void main(String args[]) {
        String testFile = "alex@gmail.com";
        UserHandler userHandler = new UserHandler();
        MailBox mailBox = userHandler.readUserMailbox(testFile);
        System.out.println(mailBox);

        Email newEmail = new Email("sender@gmail.com", List.of("recipient1@gmail.com"), "Subject", "Body", new Date(), 123);

        mailBox.getsEmails().add(newEmail);
        userHandler.writeMailbox(mailBox);
        mailBox = userHandler.readUserMailbox(testFile);
        System.out.println(mailBox);
    }
}
