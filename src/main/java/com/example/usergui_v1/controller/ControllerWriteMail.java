package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ControllerWriteMail {

    @FXML
    private TextField Destinatario;

    @FXML
    private TextField Oggetto;

    @FXML
    private TextArea Testo;

    @FXML
    private Label SuccessSend;

    @FXML
    private void Invia() throws IOException {
        Email email = new Email("test",getDestinatari(), Oggetto.getText(), Testo.getText());
        System.out.println(email);
        //JOptionPane per scrivere il pop up che ci sono stati dei problemi
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
