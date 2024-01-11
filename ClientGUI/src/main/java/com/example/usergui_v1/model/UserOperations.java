package com.example.usergui_v1.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;


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
    private Date lastUpdate;

    // constructor for all fields
    public UserOperations(Operation operation, SendType sendType, String username, Email toSend, Email reply, Email toDelete, boolean disconnect, MailBox mailBox, boolean type) {
        this.operation = operation;
        this.sendType=sendType;
        this.username = username;
        this.toSend = toSend;
        this.reply = reply;
        this.toDelete = toDelete;
        this.disconnect = disconnect;
        this.mailBox = mailBox;
        this.type = type;
    }

    // constructor for login
    public UserOperations(Operation operation, String username) {
        this(operation,null, username, null, null, null, false, null, false); // call the other constructor with default values for the other fields
    }

    // constructor for registration
    public UserOperations(Operation operation, MailBox mailBox) {
        this(operation, null, null, null, null, null, false, mailBox, false); // call the other constructor with default values for the other fields
    }
    //costructor for send email
    public UserOperations(Operation operation,SendType typeSend, String username, Email emailToSend)
    {
        this(operation,typeSend, username, emailToSend,null, null, false, null, false);
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
        } catch (ClassNotFoundException | NullPointerException e) {
            System.out.println("There is an error with the server" + e);
        }
        return null;
    }
    @Override
    public String toString() {
        return this.username;
    }


    public MailBox receiveUpdatedMailbox(ObjectInputStream in) throws IOException {
        try {
            Gson gson = new Gson();
            String mailboxJson = (String) in.readObject();
            return gson.fromJson(mailboxJson, MailBox.class);
        } catch (ClassNotFoundException e ) {
            throw new IOException("Class not found while deserializing the mailbox", e);
        }
        catch (NullPointerException e){
            throw new IOException("Null Pointer Exception error", e);
        }
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}