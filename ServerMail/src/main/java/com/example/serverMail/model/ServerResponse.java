package com.example.serverMail.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    // other methods, getters, setters as needed

    public void sendResponse(ObjectOutputStream out) throws IOException {
        Gson gson = new Gson();
        out.writeObject(gson.toJson(this));
        out.flush();
    }

    public static ServerResponse receiveResponse(ObjectInputStream in) throws IOException {
        try {
            Gson gson = new Gson();
            String result = (String) in.readObject();
            return gson.fromJson(result, ServerResponse.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
