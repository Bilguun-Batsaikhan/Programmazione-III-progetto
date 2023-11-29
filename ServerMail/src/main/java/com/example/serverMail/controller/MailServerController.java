package com.example.serverMail.controller;

import com.example.serverMail.model.MailServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class MailServerController implements Initializable {

        @FXML
        private TextArea logTextArea;

        @FXML
        private ListView<String> eventListView;
        private ArrayList<String> word = new ArrayList<>();
        Random r = new Random();
        private final MailServer model = new MailServer();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                eventListView.itemsProperty().bind(model.getListOfAction());
                ArrayWord();

        }

        // Metodo per aggiungere un evento alla lista degli eventi
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
