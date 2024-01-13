package com.example.serverMail.model;

public enum Operation {
    LOGIN(1),
    REGISTER(2),
    EXIT(3),
    SEND(4),
    RECEIVE(5),
    DELETE(6),
    UPDATE(7),
    ERROR(8);

    private final int id;

    Operation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
