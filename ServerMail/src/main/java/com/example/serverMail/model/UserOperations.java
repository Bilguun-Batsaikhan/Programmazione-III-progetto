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
    @SerializedName("reply")
    private Email reply;
    @SerializedName("toDelete")
    private Email toDelete;
    @SerializedName("disconnect")
    private boolean disconnect;

    @SerializedName("mailbox")
    private MailBox mailBox;

    @SerializedName("typeEmail")
    private boolean type;

    @SerializedName("lastUpdate")
    private final Date lastUpdate;


    // constructor for all fields
    public UserOperations(Operation operation, SendType sendType, String username, Email toSend, Email reply,
            Email toDelete, boolean disconnect, MailBox mailBox, boolean type, Date lastUpdate) {
        this.operation = operation;
        this.sendType = sendType;
        this.username = username;
        this.toSend = toSend;
        this.reply = reply;
        this.toDelete = toDelete;
        this.disconnect = disconnect;
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