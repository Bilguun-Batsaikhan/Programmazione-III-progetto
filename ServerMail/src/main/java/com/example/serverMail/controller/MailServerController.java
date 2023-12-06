package com.example.serverMail.controller;

import com.example.serverMail.model.MailServer;
import com.example.serverMail.model.UserHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ListView;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;

import java.util.Random;
import java.util.ResourceBundle;

public class MailServerController implements Initializable {

        @FXML
        private ListView<String> eventListView;
        @FXML
        private TextField searchTextField;

        private ArrayList<String> word = new ArrayList<>();
        Random r = new Random();
        private final MailServer model = new MailServer();
        private final UserHandler userHandler = new UserHandler();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                eventListView.itemsProperty().bind(model.getListOfAction());
                ArrayWord();
        }

        // Metodo per aggiungere un evento alla lista degli eventi
        @FXML
        public void addLogMessageLogin(String username) {
                model.addMessage(username);
                userHandler.addUser(username);
                // Dopo aver aggiunto un messaggio, scorri automaticamente alla fine della lista
                eventListView.scrollTo(model.getListOfAction().size() - 1);
        }

        public UserHandler getUserHandler() {
                return userHandler;
        }

        @FXML
        public void handleEventReset(ActionEvent event){
                model.resetList();
        }
        public void handleEventOrderAscending(ActionEvent event)
        {
                model.orderListAscending();
        }
        public void handleEventSearch(KeyEvent event)
        {

                String searchText = searchTextField.getText().toLowerCase();
                model.searchInList(searchText);
        }
        public void handleEventOrderDescending(ActionEvent event)
        {
                model.orderListDescending();
        }
        @FXML
        public void handleAddEvent(ActionEvent event) {
                int x = r.nextInt(word.size());
                model.addMessage(word.get(x));
                // Dopo aver aggiunto un messaggio, scorri automaticamente alla fine della lista
                eventListView.scrollTo(model.getListOfAction().size() - 1);
        }
        public void ArrayWord()
        {
                word.add("Ciao");
                word.add("Bill");
                word.add("Alex");
        }

}
