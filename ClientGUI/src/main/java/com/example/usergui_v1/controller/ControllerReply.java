package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerReply {
    @FXML
    TextField Recipients;

    @FXML
    private TextField Subject;

    @FXML
    private TextArea Body;

    @FXML
    private Label SuccessSend;

    Email selectedItem;

    String sender;

    ClientModel model;

    public void initialize(Email selectedItem,String sender, ClientModel model) {
        this.selectedItem = selectedItem;
        this.sender = sender;
        this.model = model;
    }

    public void setRecipientstoReply(){
        Recipients.setText(selectedItem.getSender());
    }


    @FXML
    private void Send(){
        ArrayList<String> recipient = new ArrayList<>();
        recipient.add(selectedItem.getSender());
        Email email = new Email(sender,recipient, Subject.getText(), Body.getText(), LocalDateTime.now(), "134223");
        model.send(email);
        SuccessSend.setText("Mail sent correctly!");
    }
}
