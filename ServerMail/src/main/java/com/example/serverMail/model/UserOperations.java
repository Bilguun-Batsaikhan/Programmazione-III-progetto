package com.example.serverMail.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.ObjectInputStream;


public class UserOperations {

    @SerializedName("operation")
    private Operation operation;
    @SerializedName("username")
    private String username;
    @SerializedName("toSend")
    private Email toSend;
    @SerializedName("toDelete")
    private Email toDelete;
    @SerializedName("disconnect")
    private boolean disconnect;

    @SerializedName("mailbox")
    private MailBox mailBox;

    // constructor for all fields
    public UserOperations(Operation operation, String username, Email toSend, Email toDelete, boolean disconnect, MailBox mailBox) {
        this.operation = operation;
        this.username = username;
        this.toSend = toSend;
        this.toDelete = toDelete;
        this.disconnect = disconnect;
        this.mailBox = mailBox;
    }

    // constructor for login
    public UserOperations(Operation operation, String username) {
        this(operation, username, null,  null, false, null); // call the other constructor with default values for the other fields
    }

    // constructor for registration
    public UserOperations(Operation operation, MailBox mailBox) {
        this(operation, null, null, null, false, mailBox); // call the other constructor with default values for the other fields
    }
    public UserOperations(Operation operation, String username, Email emailToSend)
    {
        this.operation = operation;
        this.username = username;
        this.toSend = emailToSend;
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

    public String getUsername() {
        return username;
    }


}