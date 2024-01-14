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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ControllerForward {
    @FXML
    private AnchorPane loginRoot;
    private Email selectedItem;
    private String sender;
    private ClientModel model;
    private Email email;
    @FXML
    private TextArea Recipients;
    private final SocketManager socket = new SocketManager();

    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(Email selectedItem, String sender, ClientModel model) {
        this.selectedItem = selectedItem;
        this.sender = sender;
        this.model = model;
        errorHandling(email,false,false);
    }

    @FXML
    private void Send() {
        try {
            email = new Email(sender, getRecipients(), selectedItem.getSubject(), selectedItem.getBody(), new Date(), -1);
            errorHandling(email, false, false);
            if (!getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
                socket.setUsername(sender);
                boolean sent = socket.setEmailToSend(email, SendType.FORWARD);
                errorHandling(email, true, sent);
                if (sent) {
                    handleClose();
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("There was a problem while forwarding an email " +e);
        }

    }

    private List<String> getRecipients() {
        String[] recipients_array = Recipients.getText().split(" ");
        List<String> recipients = new ArrayList<>();
        for (String s : recipients_array) {
            if (!s.isEmpty()) {
                recipients.add(s);
            }
        }
        return recipients;
    }


    private void errorHandling(Email email, boolean send,boolean success) {
        ControllerPopUp popUp = new ControllerPopUp();
        if (selectedItem == null) {
            popUp.startPopUp("NullEmail",false);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
        } else if (email != null && getRecipients().isEmpty()) {
            popUp.startPopUp("FewArguments",false);

        } else if (email != null && !model.CorrectFormatEmail(getRecipients())) {
            popUp.startPopUp("WrongFormatEmail",false);
        }
        if( email != null && send) {
            if (success) {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.hide();
                double newX = stage.getX() + 5;
                double newY = stage.getY() + 100 ;
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
