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
        private final UserHandler userHandler = new UserHandler();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                eventListView.itemsProperty().bind(model.getListOfAction());
        }

        // Metodo per aggiungere un evento alla lista degli eventi
        @FXML
        public void addLogMessage(String message) {
                model.addMessage(message);
                // Dopo aver aggiunto un messaggio, scorri automaticamente alla fine della lista
                eventListView.scrollTo(model.getListOfAction().size() - 1);
        }


        @FXML
        public void handleEventReset(){
                model.resetList();
        }
        public void handleEventOrderAscending()
        {
                model.orderListAscending();
        }
        public void handleEventSearch()
        {

                String searchText = searchTextField.getText().toLowerCase();
                model.searchInList(searchText);
        }
        public void handleEventOrderDescending()
        {
                model.orderListDescending();
        }

        @FXML
        public void readUsers() {
                List<String> usernames = userHandler.readUsers();
                String listUsers = "";
                for(String user: usernames) {
                        listUsers += user + "\n";
                }

                System.out.println(listUsers);

                userList.setText(listUsers);
        }

}
