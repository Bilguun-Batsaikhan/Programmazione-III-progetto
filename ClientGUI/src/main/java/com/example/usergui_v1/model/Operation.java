package com.example.usergui_v1.model;
public enum Operation {
    LOGIN(1),
    REGISTER(2),
    EXIT(3),
    SEND(4),
    RECEIVE(5),
    FORWARD(6),
    VERIFY(7);

    private final int id;

    Operation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
