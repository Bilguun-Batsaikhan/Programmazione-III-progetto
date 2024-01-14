package com.example.serverMail.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MailBox implements Serializable {
    private final String mailboxOwner;
    ArrayList<Email> rEmails;
    ArrayList<Email> sEmails;

    public MailBox(ArrayList<Email> rEmails, ArrayList<Email> sEmails, String me) {
        this.rEmails = rEmails;
        this.sEmails = sEmails;
        this.mailboxOwner = me;
    }

    public String getMailBoxOwner() {
        return mailboxOwner;
    }

    public ArrayList<Email> getrEmails() {
        return rEmails;
    }

    public ArrayList<Email> getsEmails() {
        return sEmails;
    }

    public void addReceivedEmail(Email email) {
        rEmails.addFirst(email);
    }

    public void addSentEmail(Email email) {
        sEmails.addFirst(email);
    }

    @Override
    public String toString() {
        StringBuilder mailboxBuilder = new StringBuilder("MailBox{ \n" + "mailBoxOwner='" + mailboxOwner + '\n' + "Received Emails: \n");
        for (Email email : rEmails) {
            mailboxBuilder.append(email).append("\n");
        }
        String mailbox = mailboxBuilder.toString();
        StringBuilder mailboxBuilder1 = new StringBuilder(mailbox + "Sent Emails: \n");
        for (Email email : sEmails) {
            mailboxBuilder1.append(email).append("\n");
        }
        mailbox = mailboxBuilder1.toString();
        return mailbox;
    }
}
