package com.example.serverMail.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.ObjectInputStream;


public class UserOperations {

    @SerializedName("numOperation")
    private int numOperation;
    @SerializedName("username")
    private String username;
    @SerializedName("toSend")
    private Email toSend;
    @SerializedName("reply")
    private Email reply;
    @SerializedName("toForward")
    private Email toForward;
    @SerializedName("disconnect")
    private boolean disconnect;

    public MailBox getMailBox() {
        return mailBox;
    }

    @SerializedName("mailbox")
    private MailBox mailBox;

    // constructor for all fields
    public UserOperations(int numOperation, String username, Email toSend, Email reply, Email toForward, boolean disconnect, MailBox mailBox) {
        this.numOperation = numOperation;
        this.username = username;
        this.toSend = toSend;
        this.reply = reply;
        this.toForward = toForward;
        this.disconnect = disconnect;
        this.mailBox = mailBox;
    }

    // constructor for login
    public UserOperations(int numOperation, String username) {
        this(numOperation, username, null, null, null, false, null); // call the other constructor with default values for the other fields
    }

    // constructor for registration
    public UserOperations(int numOperation, MailBox mailBox) {
        this(numOperation, null, null, null, null, false, mailBox); // call the other constructor with default values for the other fields
    }

    @Override
    public String toString() {
        return this.username;
    }

    public int getNumOperation() {
        return numOperation;
    }

    public String getUsername() {
        return username;
    }


}