package com.example.serverMail.model;

public enum Operation {
    LOGIN(),
    REGISTER(),
    EXIT(),
    SEND(),
    RECEIVE(),
    DELETE(),
    UPDATE(),
    ERROR();

    Operation() {
    }

}
