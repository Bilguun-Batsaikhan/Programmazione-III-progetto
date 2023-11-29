package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerReply {
    @FXML
    TextField Destinatario;

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
        Destinatario.setText(selectedItem.getSender());
    }


    @FXML
    private void Invia(){
        ArrayList<String> recipient = new ArrayList<>();
        recipient.add(selectedItem.getSender());
        Email email = new Email("test",recipient, Oggetto.getText(), Testo.getText(), LocalDateTime.now(), "134223");
        System.out.println(email);
        //TODO JOptionPane per scrivere il pop up che ci sono stati dei problemi
        SuccessSend.setText("Mail sent correctly!");
    }

    private List<String> getDestinatari() {
        String[] destinatari = Destinatario.getText().split(" ");
        List<String> recipients = new ArrayList<>();
        for (String s : destinatari) {
            if (!s.isEmpty()) {
                recipients.add(s);
            }
        }
        return recipients;
    }
}
