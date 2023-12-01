package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
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
import java.util.*;

public class ControllerWriteMail {
    @FXML
    private AnchorPane loginRoot;

    @FXML
    private TextField Recipient;

    @FXML
    private TextField Subject;

    @FXML
    private TextArea mailBody;



    String sender;
    ClientModel model;

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

        Email email = new Email(sender, getRecipients(), Subject.getText(), mailBody.getText(), LocalDateTime.now(), "134223");
        errorHandling(email);
        if((!Objects.equals(email.getBody(), "") || !Objects.equals(email.getSubject(), "")) && !getRecipients().isEmpty() && model.CorrectFormatEmail(getRecipients())) {
            model.send(email);

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

    private void startPopUp(String error) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/PopUpError.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerPopUp controller = loader.getController();
        controller.initialize(error);

        Scene newScene = new Scene(newSceneRoot);
        Stage newStage = new Stage();
        newStage.setScene(newScene);


        newScene.setFill(Color.TRANSPARENT);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: red;");
        newStage.showAndWait();

        if((!Objects.equals(error, "FewArguments")) && (!Objects.equals(error, "WrongFormatEmail"))) {
            Platform.runLater(() -> {
                Stage stage = (Stage) loginRoot.getScene().getWindow();
                stage.close();
            });
        }
    }

    private void errorHandling(Email email) throws IOException {
        if (email!=null && getRecipients().isEmpty()){
            startPopUp("FewArguments");
        }
        else if(email!=null && !model.CorrectFormatEmail(getRecipients())){
            startPopUp("WrongFormatEmail");
        }
    }

}
