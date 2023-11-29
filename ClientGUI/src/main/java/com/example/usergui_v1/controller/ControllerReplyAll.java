package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class ControllerReplyAll {
    @FXML
    private TextField Recipients;
    @FXML
    private TextField Subject;
    @FXML
    private TextArea Body;
    @FXML
    private Label SuccessSend;

    Email selectedItem;
    String sender;
    ClientModel model;

    public void initialize(Email selectedItem, String sender, ClientModel model) {
        this.selectedItem = selectedItem;
        this.sender=sender;
        this.model = model;
    }

    public void setRecipientstoReply(){
        //DA RIMUOVERE QUELLO CHE MANDA LA RISPOSTA
        Recipients.setText(selectedItem.getRecipientsString() + "  " + selectedItem.getSender());
    }

    @FXML
    private void Send(){
        Email email = new Email(sender,selectedItem.getRecipients(), Subject.getText(), Body.getText(), LocalDateTime.now(), "134223");
        model.send(email);
        SuccessSend.setText("Mail sent correctly!");
    }

}