package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.*;

public class ControllerWriteMail {

    @FXML
    private TextField Recipient;

    @FXML
    private TextField Subject;

    @FXML
    private TextArea mailBody;

    @FXML
    private Label SuccessSend;

    @FXML
    private void SendEmail(){
        Email email = new Email("test",getRecipients(), Subject.getText(), mailBody.getText(), LocalDateTime.now(), "123456");
        System.out.println(email);
        //JOptionPane per scrivere il pop up che ci sono stati dei problemi
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
