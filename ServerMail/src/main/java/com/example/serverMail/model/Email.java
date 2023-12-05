package com.example.serverMail.model;

import java.util.List;
import java.util.Date;

public class Email {
    private final String sender;
    private final List<String> recipients;
    private final String subject;
    private final String body;
    private final Date time;
    private final String ID;

    // Constructor
    public Email(String sender, List<String> recipients, String subject, String body, Date time, String id) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.time = time;
        this.ID = id;
    }

    // Getter methods
    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getRecipientsString() {
        StringBuilder recipientsString = new StringBuilder();
        for (String recipient : recipients) {
            recipientsString.append(recipient).append("   ");
        }
        return recipientsString.toString();
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time.toString();
    }

    public String getID() {
        return ID;
    }

    // Additional methods if needed
    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    public void removeRecipient(String recipient) {
        recipients.remove(recipient);
    }

    @Override
    public String toString() {
        return "Email{" +
                "sender='" + sender + '\'' +
                ", recipients=" + recipients +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", time=" + time +
                ", ID='" + ID + '\'' +
                '}';
    }
}
