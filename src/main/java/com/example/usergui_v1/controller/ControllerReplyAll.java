package com.example.usergui_v1.controller;

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

    public void setEmailtoReply(Email selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setRecipientstoReply(){
        //DA RIMUOVERE QUELLO CHE MANDA LA RISPOSTA
        Destinatario.setText(selectedItem.getRecipientsString() + "  " + selectedItem.getSender());
    }

    @FXML
    private void Invia(){
        Email email = new Email("test",selectedItem.getRecipients(), Oggetto.getText(), Testo.getText(), LocalDateTime.now(), "134223");
        System.out.println(email);
        //TODO JOptionPane per scrivere il pop up che ci sono stati dei problemi
        SuccessSend.setText("Mail sent correctly!");
    }

}
