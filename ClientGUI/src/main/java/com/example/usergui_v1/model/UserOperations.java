package com.example.usergui_v1.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.example.usergui_v1.model.Operation.EXIT;
import static com.example.usergui_v1.model.Operation.LOGIN;

public class UserOperations {

    @SerializedName("operation")
    private Operation operation;
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

    // constructor for all fields
    public UserOperations(Operation operation, String username, Email toSend, Email reply, Email toDelete, boolean disconnect, MailBox mailBox) {
        this.operation = operation;
        this.username = username;
        this.toSend = toSend;
        this.reply = reply;
        this.toDelete = toDelete;
        this.disconnect = disconnect;
        this.mailBox = mailBox;
    }

    // constructor for login
    public UserOperations(Operation operation, String username) {
        this(operation, username, null, null, null, false, null); // call the other constructor with default values for the other fields
    }

    // constructor for registration
    public UserOperations(Operation operation, MailBox mailBox) {
        this(operation, null, null, null, null, false, mailBox); // call the other constructor with default values for the other fields
    }
    //costructor for send email
    public UserOperations(Operation operation, String username, Email emailToSend)
    {
        this(operation,username,emailToSend, null, null, false, null);
    }

    public void sendRequest(ObjectOutputStream out) {
        assert this.operation != null : "Operation cannot be NULL";
        try {
            Gson gson = new Gson();
            out.writeObject(gson.toJson(this));
            out.flush();
        } catch (IOException | RuntimeException e) {
            System.out.println("There is a problem while sending username " + e);
        }
    }


    public ServerResponse receiveServerResponse(ObjectInputStream in) throws IOException {
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