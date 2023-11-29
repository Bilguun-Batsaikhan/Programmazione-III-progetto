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
    private TextField Destinatario;
    @FXML
    private TextField Oggetto;
    @FXML
    private TextArea Testo;
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
        Destinatario.setText(selectedItem.getRecipientsString() + "  " + selectedItem.getSender());
    }

    @FXML
    private void Invia(){
        Email email = new Email(sender,selectedItem.getRecipients(), Oggetto.getText(), Testo.getText(), LocalDateTime.now(), "134223");
        model.send(email);
        SuccessSend.setText("Mail sent correctly!");
    }

}
