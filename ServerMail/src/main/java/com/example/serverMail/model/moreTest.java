package com.example.serverMail.model;

public class moreTest {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "moreTest{" +
                "name='" + name + '\'' +
                '}';
    }

    public moreTest(String name) {
        this.name = name;
    }
}

