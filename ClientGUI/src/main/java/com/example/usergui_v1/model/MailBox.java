package com.example.usergui_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
public class MailBox implements Serializable {
    ArrayList<Email> rEmails;
    ArrayList<Email> sEmails;
    String mailboxOwner;

    public MailBox(ArrayList<Email> rEmails, ArrayList<Email> sEmails, String me) {
        this.rEmails = rEmails;
        this.sEmails = sEmails;
        this.mailboxOwner = me;
    }

    public ArrayList<Email> getrEmails() {
        return rEmails;
    }

    public ArrayList<Email> getsEmails() {
        return sEmails;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MailBox other = (MailBox) obj;
        return rEmails.equals(other.rEmails) && sEmails.equals(other.sEmails);
    }
}

