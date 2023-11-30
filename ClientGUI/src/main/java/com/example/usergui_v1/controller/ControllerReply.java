package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ControllerReply {

    @FXML
    AnchorPane loginRoot;

    @FXML
    TextField Recipients;

    @FXML
    private TextField Subject;

    @FXML
    private TextArea Body;

    @FXML
    private Label SuccessSend;

    Email selectedItem;

    String sender;

    ClientModel model;

    Email email;

    @FXML
    private void handleClose(MouseEvent event) {
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

    public void setRecipientstoReply() throws IOException {
        if (selectedItem != null) {
            Recipients.setText(selectedItem.getSender());
        }
    }

    private void errorHandling(Email email) throws IOException {
        if(selectedItem == null){
            startPopUp("NullEmail");
        }
        else if (Objects.equals(selectedItem.getSender(), model.mailBoxOwnerProperty().get())) {
          startPopUp("ReplySent");
        }
        else if (email!=null && Objects.equals(email.getBody(), "") && Objects.equals(email.getSubject(), "")){
            startPopUp("FewArguments");
        }
    }
    private void startPopUp(String error) throws IOException {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/PopUp.fxml")));
            Parent newSceneRoot = loader.load();
            ControllerPopUp controller = loader.getController();
            controller.initialize(error);

            Scene newScene = new Scene(newSceneRoot);
            Stage newStage = new Stage();
            newStage.setScene(newScene);

            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.showAndWait();

            if(!Objects.equals(error, "FewArguments")) {
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

        email = new Email(sender,recipient, Subject.getText(), Body.getText(), LocalDateTime.now(), "134223");
        errorHandling(email);
        if(!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) {
            model.send(email);
            SuccessSend.setText("Mail sent correctly!");
        }
    }
}
