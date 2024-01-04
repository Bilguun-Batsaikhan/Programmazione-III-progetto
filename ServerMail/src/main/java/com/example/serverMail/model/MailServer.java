package com.example.serverMail.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MailServer {
    private SimpleListProperty<String> listOfAction;
    private ObservableList<String> data;

    public MailServer() {
        data = FXCollections.observableArrayList();
        listOfAction = new SimpleListProperty<>(data);
    }

    public SimpleListProperty<String> getListOfAction() {
        return listOfAction;
    }

    public void addMessage(String action) {
        data.add(action);
    }

    public void resetList()
    {
        data.clear();
    }
    public void orderListAscending() {
        data.sort(String::compareToIgnoreCase);
    }

    public void orderListDescending() {
        data.sort((s1, s2) -> s2.compareToIgnoreCase(s1));
    }
    public void searchInList(String found)
    {
        ObservableList<String> filteredList = data.filtered(item -> item.toLowerCase().contains(found));
        listOfAction.set(filteredList);
    }
}

