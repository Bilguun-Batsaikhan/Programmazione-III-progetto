package com.example.serverMail.controller;

import com.example.serverMail.model.MailServer;
import com.example.serverMail.model.UserHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MailServerController implements Initializable {

        @FXML
        private Button userReaderBtn;
        @FXML
        private TextArea userList;
        @FXML
        private TextField newUser;
        @FXML
        private TextField logTextField;

        @FXML
        private Button addUserBtn;
        @FXML
        private ListView<String> eventListView;
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
        public void handleAddEvent(ActionEvent event) {
                int x = r.nextInt(word.size());
                model.addMessage(word.get(x));
                // Dopo aver aggiunto un messaggio, scorri automaticamente alla fine della lista
                eventListView.scrollTo(model.getListOfAction().size() - 1);
        }
        @FXML
        public void addUser(ActionEvent event) {
                String userToAdd = newUser.getText();
                if(!userToAdd.equals(null)) {
                        Boolean result = userHandler.addUser(userToAdd);
                        if(result) {
                                System.out.println("new user " + userToAdd + " successfully added");
                        } else {
                                System.out.println("There is a problem...");
                        }
                }
        }
        @FXML
        public void readUsers(ActionEvent event) {
                List<String> users = userHandler.readUsers();
                String listUsers = " ";
                for(String user: users) {
                        listUsers += user + " ";
                }

                System.out.println(listUsers);

                userList.setText(listUsers);
        }
        public void ArrayWord()
        {
                word.add("Ciao");
                word.add("Bill");
                word.add("Alex");
        }
}
