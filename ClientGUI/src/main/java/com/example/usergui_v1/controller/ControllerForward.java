package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private SocketManager socket = new SocketManager();

    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(Email selectedItem, String sender, ClientModel model) throws IOException {
        this.selectedItem = selectedItem;
        this.sender = sender;
        this.model = model;
        errorHandling(email);
    }

    @FXML
    private void Send() throws IOException {
        email = new Email(sender, getRecipients(), selectedItem.getSubject(), selectedItem.getBody(), new Date(), "134223");
        errorHandling(email);
        if (!getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
            socket.setEmailToSend(email);
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


    private void errorHandling(Email email) throws IOException {
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
    }
}
