package com.example.serverMail.controller;

import com.example.serverMail.model.MailServer;
import com.example.serverMail.model.UserHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MailServerController implements Initializable {
        @FXML
        private ListView<String> eventListView;
        @FXML
        private TextField searchTextField;
        @FXML
        private TextArea userList;

        private final MailServer model = new MailServer();
        private UserHandler userHandler;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                eventListView.itemsProperty().bind(model.getListOfAction());
        }

        // Method to add an event to the event list
        @FXML
        public void addLogMessage(String message) {
                model.addMessage(message);
                // After adding a message, automatically scroll to the end of the list
                eventListView.scrollTo(model.getListOfAction().size() - 1);
        }

        @FXML
        public void handleEventReset() {
                model.resetList();
        }

        // Method to handle ordering events in ascending order
        public void handleEventOrderAscending() {
                model.orderListAscending();
        }

        // Method to handle searching for events
        public void handleEventSearch() {
                String searchText = searchTextField.getText().toLowerCase();
                model.searchInList(searchText);
        }

        // Method to handle ordering events in descending order
        public void handleEventOrderDescending() {
                model.orderListDescending();
        }

        @FXML
        public void readUsers() {
                List<String> usernames = userHandler.readUsers();
                String listUsers = "";
                for (String user : usernames) {
                        listUsers += user + "\n";
                }

                System.out.println(listUsers);

                userList.setText(listUsers);
        }

        public void setUserHandler(UserHandler userHandler) {
                this.userHandler = userHandler;
        }
}
