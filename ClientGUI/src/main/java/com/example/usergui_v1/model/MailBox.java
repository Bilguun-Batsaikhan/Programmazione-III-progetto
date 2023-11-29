package com.example.usergui_v1.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MailBox {
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

        ArrayList<String> emailList = new ArrayList<>();
        emailList.add("dfd@gmail.com");
        emailList.add("dfs@gmail.it");
        emailList.add("dfd@gmail.com");

        mailBoxOwner = "defualt@edu.unito.com";
        rEmails.add(new Email(mailBoxOwner, emailList, "first email", "Vediamo1", LocalDateTime.now(), "134223"));
        rEmails.add(new Email(mailBoxOwner, emailList, "second email", "Vediamo2", LocalDateTime.now(), "134223"));
        rEmails.add(new Email(mailBoxOwner, emailList, "third email", "Vediamo3", LocalDateTime.now(), "134223"));
        rEmails.add(new Email(mailBoxOwner, emailList, "fourth email", "Vediamo4", LocalDateTime.now(), "134223"));


        sEmails.add(new Email(mailBoxOwner, emailList, "first email", "Vediamo", LocalDateTime.now(), "134223"));
        sEmails.add(new Email(mailBoxOwner, emailList, "second email", "Vediamo", LocalDateTime.now(), "134223"));
        sEmails.add(new Email(mailBoxOwner, emailList, "third email", "Vediamo", LocalDateTime.now(), "134223"));
        sEmails.add(new Email(mailBoxOwner, emailList, "fourth email", "Vediamo", LocalDateTime.now(), "134223"));
        
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

    public ArrayList<String> getrSubjectsEmail() {
        ArrayList<String> emailSubjects = new ArrayList<>();
        for(Email e: rEmails) {
            emailSubjects.add(e.getSubject());
        }
        return emailSubjects;
    }
}
