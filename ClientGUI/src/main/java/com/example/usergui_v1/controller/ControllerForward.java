package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerForward {
    Email selectedItem;
    String sender;
    ClientModel model;

    @FXML
    private TextField Recipients;

    public void initialize(Email selectedItem, String sender, ClientModel model) {
        this.selectedItem = selectedItem;
        this.sender=sender;
        this.model = model;
    }

    @FXML
    private void Invia(){
        Email email = new Email(sender,getRecipients(), selectedItem.getSubject(), selectedItem.getBody(), LocalDateTime.now(), "134223");
        model.send(email);
        //TODO JOptionPane per scrivere il pop up che ci sono stati dei problemi
    }

    private List<String> getRecipients() {
        String[] recipients_array = Recipients.getText().split(" ");
        List<String> recipients = new ArrayList<>();
        for (String s : recipients_array) {
            if (!s.isEmpty()) {
                recipients.add(s);
            }
        }
        return recipients;
    }



}