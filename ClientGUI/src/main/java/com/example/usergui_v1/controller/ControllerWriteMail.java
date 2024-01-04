package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SendType;
import com.example.usergui_v1.model.SocketManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
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

    private final SocketManager socket = new SocketManager();



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

        Email email = new Email(sender, getRecipients(), Subject.getText(), mailBody.getText(), new Date(), -1);
        errorHandling(email,false,false);
        if((!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) && !getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
            socket.setUsername(sender);
            boolean sent = socket.setEmailToSend(email, SendType.SEND);
            errorHandling(email,true, sent);
            if(sent) {
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

    private void errorHandling(Email email,boolean send,boolean success) throws IOException {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && (getRecipients().isEmpty() ||  Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), ""))){
            popUp.startPopUp("FewArguments",false);
        }
        else if(email!=null && !model.CorrectFormatEmail(getRecipients())){
            popUp.startPopUp("WrongFormatEmail",false);
        }
        if(send) {
            if (success) {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.hide();
                popUp.startPopUp("MailSent",true);
            } else {
                popUp.startPopUp("EmailNotExist", false);
            }
        }
    }

}
