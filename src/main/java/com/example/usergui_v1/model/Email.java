package com.example.usergui_v1.model;

import java.util.List;

public class Email {
    private final String sender;
    private final List<String> recipients;
    private final String subject;
    private final String body;

    // Constructor
    public Email(String sender, List<String> recipients, String subject, String body) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
    }

    // Getter methods
    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
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
                '}';
    }
}
