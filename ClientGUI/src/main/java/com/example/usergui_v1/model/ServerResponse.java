package com.example.usergui_v1.model;

import com.google.gson.annotations.SerializedName;

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

    public boolean isNotSuccess() {
        return !success;
    }

    public String getMessage() {
        return message;
    }


}
