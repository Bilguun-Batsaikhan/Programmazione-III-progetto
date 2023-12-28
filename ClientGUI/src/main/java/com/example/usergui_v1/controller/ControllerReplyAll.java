package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SendType;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
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
    private ClientModel model;
    private Email email;
    private final SocketManager socket = new SocketManager();


    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(Email selectedItem, String sender, ClientModel model) throws IOException {
        this.selectedItem = selectedItem;
        this.sender = sender;
        this.model = model;
        errorHandling(email,false,false);
        setRecipientsToReply();

    }

    private void setRecipientsToReply(){
        if(selectedItem!=null) {
            selectedItem.getRecipients().remove(sender);
            String recipients = selectedItem.getRecipientsString();
            System.out.println(recipients);
            Recipients.setText(recipients + "" + selectedItem.getSender());
        }
    }

    @FXML
    private void Send() throws IOException {
        email = new Email(sender,selectedItem.getRecipients(), Subject.getText(), Body.getText(), new Date(), -1);
        errorHandling(email,false,false);
        if(!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) {
            socket.setUsername(sender);
            boolean sent = socket.setEmailToSend(email, SendType.REPLYALL);
            errorHandling(email,true, sent);
            if(sent) {
                handleClose();
            }
        }
    }


    private void errorHandling(Email email,boolean send,boolean success) throws IOException {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), "")){
            popUp.startPopUp("FewArguments",false);
        }
        if (selectedItem == null) {
            popUp.startPopUp("NullEmail",false);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });

        } else if (Objects.equals(selectedItem.getSender(), model.mailBoxOwnerProperty().get())) {
            popUp.startPopUp("ReplySent",false);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
        }
        if(send) {
            if (success) {
                popUp.startPopUp("MailSent",true);
            } else {
                popUp.startPopUp("EmailNotExist", false);
            }
        }
        }


}
