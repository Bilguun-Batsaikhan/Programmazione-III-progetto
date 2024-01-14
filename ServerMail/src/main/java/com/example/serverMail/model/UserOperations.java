package com.example.serverMail.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserOperations {
    @SerializedName("operation")
    private Operation operation;

    @SerializedName("sendType")
    private SendType sendType;
    @SerializedName("username")
    private String username;
    @SerializedName("toSend")
    private Email toSend;
    @SerializedName("toDelete")
    private Email toDelete;

    @SerializedName("mailbox")
    private MailBox mailBox;

    @SerializedName("typeEmail")
    private boolean type;

    @SerializedName("lastUpdate")
    private final Date lastUpdate;


    // constructor for all fields
    public UserOperations(Operation operation, SendType sendType, String username, Email toSend,
                          Email toDelete, MailBox mailBox, boolean type, Date lastUpdate) {
        this.operation = operation;
        this.sendType = sendType;
        this.username = username;
        this.toSend = toSend;
        this.toDelete = toDelete;
        this.mailBox = mailBox;
        this.type = type;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return this.username;
    }

    public Operation getOperation() {
        return operation;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public Email getToSend() {
        return toSend;
    }

    public Email getToDelete() {
        return toDelete;
    }

    public boolean getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public SendType getSendType() { return sendType;}

    public Date getLastUpdate() { return lastUpdate; }
}