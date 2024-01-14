package com.example.serverMail.model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// This class acts as a model of Mail Server Controller, meaning its instance can be found in the controller
public class MailServer {
    // List of actions to be displayed in the GUI, it's bound to the ListView in the
    // controller
    private final SimpleListProperty<String> listOfAction;
    // Observable list cannot be bound to the ListView, so we use a
    // SimpleListProperty
    private final ObservableList<String> data;

    public MailServer() {
        data = FXCollections.observableArrayList();
        listOfAction = new SimpleListProperty<>(data);
    }

    /**
     * Get the list of actions.
     *
     * @return the list of actions
     */
    public SimpleListProperty<String> getListOfAction() {
        return listOfAction;
    }

    /**
     * Add a message to the list of actions.
     *
     * @param action the action to add
     */
    public void addMessage(String action) {
        data.add(action);
    }

    /**
     * Reset the list of actions.
     */
    public void resetList() {
        data.clear();
    }

    /**
     * Order the list of actions in ascending order.
     */
    public void orderListAscending() {
        data.sort(String::compareToIgnoreCase);
    }

    /**
     * Order the list of actions in descending order.
     */
    public void orderListDescending() {
        data.sort((s1, s2) -> s2.compareToIgnoreCase(s1));
    }

    /**
     * Search for a specific string in the list of actions and update the list to
     * show only matching items.
     *
     * @param found the string to search for
     */
    public void searchInList(String found) {

        // FilteredList will contain all items from data that, when converted to lower
        // case, contain the string found
        ObservableList<String> filteredList = data.filtered(item -> item.toLowerCase().contains(found));
        listOfAction.set(filteredList);
    }
}
