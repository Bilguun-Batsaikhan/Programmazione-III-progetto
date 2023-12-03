package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.*;

public class ControllerWriteMail {
    @FXML
    AnchorPane loginRoot;

    @FXML
    private TextField Recipient;

    @FXML
    private TextField Subject;

    @FXML
    private TextArea mailBody;

    @FXML
    private Label SuccessSend;

    String sender;
    ClientModel model;

    public void initialize(String sender, ClientModel model) {
        this.sender = sender;
        this.model = model;

    }

    @FXML
    private void handleClose(MouseEvent event) {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void SendEmail(){

        Email email = new Email(sender, getRecipients(), Subject.getText(), mailBody.getText(), LocalDateTime.now(), "134223");
        model.send(email);
      
        SuccessSend.setText("Mail sent correctly!");
    }

    private List<String> getRecipients() {
        String[] destinatari = Recipient.getText().split(" ");
        List<String> recipients = new ArrayList<>();
        for (String s : destinatari) {
            if (!s.isEmpty()) {
                recipients.add(s);
            }
        }
        return recipients;
    }

}
