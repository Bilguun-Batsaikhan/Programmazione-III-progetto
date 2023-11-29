package com.example.serverMail.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MailServer {
    private SimpleListProperty<String> listOfAction;

    public MailServer() {
        ObservableList<String> data = FXCollections.observableArrayList();
        listOfAction = new SimpleListProperty<>(data);
    }

    public SimpleListProperty<String> getListOfAction() {
        return listOfAction;
    }

    public void addMessage(String action) {
        listOfAction.add(action);
    }
}

