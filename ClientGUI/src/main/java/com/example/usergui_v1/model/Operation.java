package com.example.usergui_v1.model;
public enum Operation {
    LOGIN(1),
    REGISTER(2),
    EXIT(3),
    SEND(4),
    DELETE(6),
    UPDATE(7);

    private final int id;

    Operation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
