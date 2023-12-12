package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ControllerReply {

    @FXML
    private AnchorPane loginRoot;

    @FXML
    private TextField Recipients;

    @FXML
    private TextArea Subject;

    @FXML
    private TextArea Body;

    private Email selectedItem;

    private String sender;

    private ClientModel model;

    private Email email;

    private SocketManager socket = new SocketManager();


    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }


    public void initialize(Email selectedItem,String sender, ClientModel model) throws IOException {
        this.selectedItem = selectedItem;
        this.sender = sender;
        this.model = model;
        errorHandling(email);
        setRecipientstoReply();

    }

    private void setRecipientstoReply() {
        if (selectedItem != null) {
            Recipients.setText(selectedItem.getSender());
        }
    }

    private void errorHandling(Email email) throws IOException {
        ControllerPopUp popUp = new ControllerPopUp();
        if (email!=null && Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), "")){
            popUp.startPopUp("FewArguments",false);
        }
        else {
            if (selectedItem == null) {
                popUp.startPopUp("NullEmail",false);
            } else if (Objects.equals(selectedItem.getSender(), model.mailBoxOwnerProperty().get())) {
                popUp.startPopUp("ReplySent",false);
            }
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
        }
    }

    @FXML
    private void Send() throws IOException {
        ArrayList<String> recipient = new ArrayList<>();
        recipient.add(selectedItem.getSender());

        email = new Email(sender,recipient, Subject.getText(), Body.getText(), new Date(), "134223");
        errorHandling(email);
        if(!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) {
            socket.setEmailToSend(email);
        }
    }
}
