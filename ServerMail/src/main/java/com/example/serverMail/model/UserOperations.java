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