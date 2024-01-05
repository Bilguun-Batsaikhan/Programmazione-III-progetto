package com.example.serverMail.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MailBox implements Serializable {
    private String mailboxOwner;
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
        rEmails.add(0,email);
    }

    public void addSentEmail(Email email) {
        sEmails.add(0,email);
    }

    @Override
    public String toString() {
        String mailbox = "MailBox{ \n" + "mailBoxOwner='" + mailboxOwner + '\n' + "Received Emails: \n";
        for (Email email : rEmails) {
            mailbox = mailbox + email + "\n";
        }
        mailbox = mailbox + "Sent Emails: \n";
        for (Email email : sEmails) {
            mailbox = mailbox + email + "\n";
        }
        return mailbox;
    }
}
