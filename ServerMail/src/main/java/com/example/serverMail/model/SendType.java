package com.example.serverMail.model;

public enum SendType {

        SEND(1),
        FORWARD(2),
        REPLY(3),
        REPLYALL(4);

        private final int type;

        SendType(int type) {
            this.type = type;
        }

}

