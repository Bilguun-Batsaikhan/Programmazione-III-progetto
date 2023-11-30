package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class ControllerForward {
    @FXML
    AnchorPane loginRoot;
    Email selectedItem;
    String sender;
    ClientModel model;
    Email email;
    @FXML
    private TextField Recipients;

    @FXML
    private void handleClose(MouseEvent event) {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(Email selectedItem, String sender, ClientModel model) throws IOException {
        this.selectedItem = selectedItem;
        this.sender=sender;
        this.model = model;
        errorHandling(email);
    }

    @FXML
    private void Send() throws IOException {
        email = new Email(sender,getRecipients(), selectedItem.getSubject(), selectedItem.getBody(), LocalDateTime.now(), "134223");
        errorHandling(email);
        if(!getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
            model.send(email);
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

        if((!Objects.equals(error, "FewArguments")) && (!Objects.equals(error, "WrongFormatEmail"))) {
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
        }
    }

    private void errorHandling(Email email) throws IOException {
        if(selectedItem == null){
            startPopUp("NullEmail");
        }
        else if (email!=null && getRecipients().isEmpty()){
            startPopUp("FewArguments");
        }
        else if(email!=null && !model.CorrectFormatEmail(getRecipients())){
            startPopUp("WrongFormatEmail");
        }
    }

}
