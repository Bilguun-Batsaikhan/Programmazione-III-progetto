package com.example.usergui_v1.controller;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.SendType;
import com.example.usergui_v1.model.SocketManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    private Email email;

    private final SocketManager socket = new SocketManager();


    @FXML
    private void handleClose() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }


    public void initialize(Email selectedItem, String sender) {
        this.selectedItem = selectedItem;
        this.sender = sender;
        errorHandling(email,false,false);
        setRecipientsToReply();

    }

    private void setRecipientsToReply() {
        if (selectedItem != null) {
            Recipients.setText(selectedItem.getSender());
        }
    }

    private void errorHandling(Email email,boolean send,boolean success) {
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

        }
        if (Objects.equals(selectedItem.getSender(), sender)) {
            popUp.startPopUp("ReplySent",false);
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
            }
        if( email != null && send) {
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

    @FXML
    private void Send() {
        try {
            ArrayList<String> recipient = new ArrayList<>();
            recipient.add(selectedItem.getSender());

            email = new Email(sender, recipient, Subject.getText(), Body.getText(), new Date(), -1);
            errorHandling(email, false, false);
            if (!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) {
                socket.setUsername(sender);
                boolean sent = socket.setEmailToSend(email, SendType.REPLY);
                errorHandling(email, true, sent);
                if (sent) {
                    handleClose();
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("There was a problem while replying an email " +e);
        }
    }
}
