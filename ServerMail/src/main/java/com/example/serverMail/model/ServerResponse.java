package com.example.serverMail.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ServerResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // constructor for success response
    public ServerResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void sendMailbox(MailBox mailbox, ObjectOutputStream out) throws IOException {
        Gson gson = new Gson();
        String mailboxJson = gson.toJson(mailbox);
        out.writeObject(mailboxJson);
        out.flush();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
