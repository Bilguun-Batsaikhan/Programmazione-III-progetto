package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SendType;
import com.example.usergui_v1.model.SocketManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    private void SendEmail()  {
        try{
        List<String> recipients = model.getRecipients(Recipient.getText());
        Email email = new Email(sender, recipients, Subject.getText(), mailBody.getText(), new Date(), -1);
        errorHandling(email,false,false);
        if((!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) && !recipients.isEmpty() && model.CorrectFormatEmail(recipients)) {
            socket.setUsername(sender);
            boolean sent = socket.setEmailToSend(email, SendType.SEND);
            errorHandling(email,true, sent);
            if(sent) {
                handleClose();
            }
        }}
        catch (NullPointerException e){
                System.out.println("There was a problem while sending an email " +e);
            }
    }

    private void errorHandling(Email email,boolean send,boolean success)  {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && (model.getRecipients(Recipient.getText()).isEmpty() ||  Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), ""))){
            popUp.startPopUp("FewArguments",false);
        }
        else if(email!=null && !model.CorrectFormatEmail(model.getRecipients(Recipient.getText()))){
            popUp.startPopUp("WrongFormatEmail",false);
        }
        if(email != null && send) {
            if (success) {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.hide();
                double newX = stage.getX() + 5;
                double newY = stage.getY() + 400 ;
                popUp.setPosition(newX, newY);
                popUp.startPopUp("MailSent",true);
            } else {
                boolean recipientSame = email.getRecipients().stream().anyMatch(recipient -> Objects.equals(recipient, email.getSender()));
                if(recipientSame) {
                    popUp.startPopUp("SameSender", false);
                }
                else{
                    popUp.startPopUp("EmailNotExist", false);
                }
            }
        }
    }

}
