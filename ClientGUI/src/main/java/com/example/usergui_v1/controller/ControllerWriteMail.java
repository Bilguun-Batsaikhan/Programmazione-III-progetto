package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.Operation;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ControllerWriteMail {
    @FXML
    private AnchorPane loginRoot;

    @FXML
    private TextArea Recipient;

    @FXML
    private TextArea Subject;

    @FXML
    private TextArea mailBody;

    private String sender;
    private ClientModel model;

    private SocketManager socket = new SocketManager();



    public void initialize(String sender, ClientModel model) {
        this.sender = sender;
        this.model = model;
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void SendEmail() throws IOException {

        Email email = new Email(sender, getRecipients(), Subject.getText(), mailBody.getText(), new Date(), "134223");
        errorHandling(email);
        if((!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) && !getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
            socket.setUsername(sender);
            socket.setEmailToSend(email);
            boolean send = socket.startSocket(Operation.SEND);
            if(send) {
                Platform.runLater(() -> {
                    model.addSentEmail(email);
                    this.handleClose();
                    List<Email> pritnSend = model.getSendEmail();
                    System.out.println(pritnSend.toString());
                });

            }
            else {
                System.out.println("Utenti errati");
                handleClose();
            }
        }
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

    private void errorHandling(Email email) throws IOException {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && (getRecipients().isEmpty() ||  Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), ""))){
            popUp.startPopUp("FewArguments",false);
        }
        else if(email!=null && !model.CorrectFormatEmail(getRecipients())){
            popUp.startPopUp("WrongFormatEmail",false);
        }

    }

}
