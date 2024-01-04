package com.example.usergui_v1.model;
import java.util.List;
import java.util.Date;
import java.util.Objects;

public class Email {
    private final String sender;
    private final List<String> recipients;
    private final String subject;
    private final String body;
    private final Date time;
    private final int ID;

    // Constructor
    public Email(String sender, List<String> recipients, String subject, String body, Date time, int id) {
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
            recipientsString.append(recipient).append(" ");
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

    public int getID() {
        return ID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return Objects.equals(sender, email.sender) &&
                Objects.equals(recipients, email.recipients) &&
                Objects.equals(subject, email.subject) &&
                Objects.equals(body, email.body) &&
                Objects.equals(time, email.time) &&
                Objects.equals(ID, email.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipients, subject, body, time, ID);
    }


}
