package com.example.serverMail.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MailBox implements Serializable {
    private final String mailBoxOwner;
    ArrayList<Email> rEmails;
    ArrayList<Email> sEmails;

    public MailBox(ArrayList<Email> rEmails, ArrayList<Email> sEmails, String me) {
        this.rEmails = rEmails;
        this.sEmails = sEmails;
        this.mailBoxOwner = me;
    }
    public MailBox() {
        this.rEmails = new ArrayList<>();
        this.sEmails = new ArrayList<>();

        mailBoxOwner = "defualt@edu.unito.com";
    }

    public String getMailBoxOwner() {
        return mailBoxOwner;
    }

    public ArrayList<Email> getrEmails() {
        return rEmails;
    }

    public void setrEmails(ArrayList<Email> rEmails) {
        this.rEmails = rEmails;
    }
    //i metodi per prendere lista degli email ecc
    public ArrayList<Email> getsEmails() {
        return sEmails;
    }

    public void setsEmails(ArrayList<Email> sEmails) {
        this.sEmails = sEmails;
    }

    public void addReceivedEmail(Email email) {
        rEmails.add(0,email);
    }

    public void addSentEmail(Email email) {
        sEmails.add(0,email);
    }
    public ArrayList<String> getrSubjectsEmail() {
        ArrayList<String> emailSubjects = new ArrayList<>();
        for(Email e: rEmails) {
            emailSubjects.add(e.getSubject());
        }
        return emailSubjects;
    }

    @Override
    public String toString() {
        String mailbox = "MailBox{ \n" + "mailBoxOwner='" + mailBoxOwner + '\n' + "Received Emails: \n";
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
