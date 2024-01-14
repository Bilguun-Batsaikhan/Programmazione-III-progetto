package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SendType;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ControllerReplyAll {
    @FXML
    private AnchorPane loginRoot;
    @FXML
    private TextArea Recipients;
    @FXML
    private TextArea Subject;
    @FXML
    private TextArea Body;
    private Email selectedItem;
    private String sender;
    private Email email;
    private final SocketManager socket = new SocketManager();

    private final ArrayList<String> emailToRespond = new ArrayList<>();

    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(Email selectedItem, String sender)  {
        this.selectedItem = selectedItem;
        this.sender = sender;
        errorHandling(email,false,false);
        setRecipientsToReply();

    }

    private void setRecipientsToReply(){
        if(selectedItem!=null) {
            selectedItem.getRecipients().remove(sender);
            String recipients = selectedItem.getRecipientsString();
            emailToRespond.addAll(selectedItem.getRecipients());
            Recipients.setText(recipients + selectedItem.getSender());
            emailToRespond.add(selectedItem.getSender());
        }
    }

    @FXML
    private void Send()  {
        try{email = new Email(sender,emailToRespond, Subject.getText(), Body.getText(), new Date(), -1);
        errorHandling(email,false,false);
        if(!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) {
            socket.setUsername(sender);
            boolean sent = socket.setEmailToSend(email, SendType.REPLYALL);
            errorHandling(email,true, sent);
            if(sent) {
                handleClose();
            }
        }}
        catch (NullPointerException | IOException  e){
            System.out.println("There was a problem while replying an email " +e);
        }
    }


    private void errorHandling(Email email,boolean send,boolean success) {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), "")){
            popUp.startPopUp("FewArguments",success);
        }
        if (selectedItem == null) {
            popUp.startPopUp("NullEmail",success);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });

        } else if (Objects.equals(selectedItem.getSender(), sender)) {
            popUp.startPopUp("ReplySent",success);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
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
                popUp.startPopUp("EmailNotExist", false);
            }
        }
        }


}
