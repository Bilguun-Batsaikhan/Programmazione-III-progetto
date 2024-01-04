package com.example.usergui_v1.model;

public enum SendType {

    SEND(1),
    FOWARD(2),
    REPLY(3),
    REPLYALL(4);

    public int getType() {
        return type;
    }

    private final int type;

    SendType(int type) {
        this.type = type;
    }

}
