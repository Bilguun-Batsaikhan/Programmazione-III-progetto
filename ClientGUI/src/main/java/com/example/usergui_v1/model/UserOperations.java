package com.example.usergui_v1.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public void sendLoginRequest(ObjectOutputStream out) {
        assert this.numOperation == 1 && this.username != null : "Username cannot be NULL";
        try {
            Gson gson = new Gson();
            out.writeObject(gson.toJson(this));
            out.flush();
        } catch (IOException e) {
            System.out.println("There is a problem while sending username " + e);
        }
    }

    public void sendRegistrationRequest(ObjectOutputStream out) {
        assert this.mailBox != null : "Mailbox cannot be NULL";
        try {
            Gson gson = new Gson();
            out.writeObject(gson.toJson(this));
            out.flush();
        } catch (IOException e) {
            System.out.println("There is problem while sending mailbox " + e);
        }
    }
    public void sendLeftRequest(ObjectOutputStream out) {
        assert this.numOperation == 3 && this.username != null : "Username cannot be NULL";
        try {
            Gson gson = new Gson();
            out.writeObject(gson.toJson(this));
            out.flush();
        } catch (IOException e) {
            System.out.println("There is a problem while sending username " + e);
        }
    }

    public ServerResponse receiveLoginAuthentication(ObjectInputStream in) throws IOException {
        try {
            Gson x = new Gson();
            String result = (String) in.readObject();
            return x.fromJson(result, ServerResponse.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.username;
    }


}