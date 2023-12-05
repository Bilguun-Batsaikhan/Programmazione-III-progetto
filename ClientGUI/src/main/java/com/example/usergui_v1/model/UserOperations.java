package com.example.usergui_v1.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class UserOperations {
    private static final long serialVersionUID = 1L;

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

    // constructor for all fields
    public UserOperations(int numOperation, String username, Email toSend, Email reply, Email toForward, boolean disconnect) {
        this.numOperation = numOperation;
        this.username = username;
        this.toSend = toSend;
        this.reply = reply;
        this.toForward = toForward;
        this.disconnect = disconnect;
    }

    // constructor for only two fields
    public UserOperations(int numOperation, String username) {
        this(numOperation, username, null, null, null, false); // call the other constructor with default values for the other fields
    }

    public void sendLoginRequest(ObjectOutputStream out) throws IOException {
        // Create Email object
        //Email e = new Email("Bilguun", null, "test", "one two three", new Date(), "1");
        Gson gson = new Gson();

        out.writeObject(gson.toJson(this));
        out.flush();
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

    public int getNumOperation() {
        return numOperation;
    }

    public void setNumOperation(int numOperation) {
        this.numOperation = numOperation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Email getToSend() {
        return toSend;
    }

    public void setToSend(Email toSend) {
        this.toSend = toSend;
    }

    public Email getReply() {
        return reply;
    }

    public void setReply(Email reply) {
        this.reply = reply;
    }

    public Email getToForward() {
        return toForward;
    }

    public void setToForward(Email toForward) {
        this.toForward = toForward;
    }

    public boolean isDisconnect() {
        return disconnect;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

}