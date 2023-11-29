package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.MailBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_List implements Initializable {
    @FXML
    private Label Destinatari;

    @FXML
    private Label Oggetto;

    @FXML
    private Label Testo;

    @FXML
    private Label Mittente;

    @FXML
    private Label Data;

    @FXML
    private ListView<Email> emailRlist;
    @FXML
    private ListView<Email> emailSlist;
    private final MailBox mailBox = new MailBox();

    //TODO PASSARE NEI VARI METODI IL MITTENTE
    @FXML
    private void WriteEmail() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/WriteEmail.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 500);

        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Nuova Mail");
        newStage.show();
    }

    @FXML
    private void Inoltra() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Forward.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerForward controller = loader.getController();
        controller.setEmailtoReply(currentEmail);

        Scene newScene = new Scene(newSceneRoot, 450 , 200);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Inoltra");
        newStage.show();
    }

    @FXML
    private void Reply() throws IOException {
        Email selectedItem = emailRlist.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Reply.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerReply controller = loader.getController();
        controller.setEmailtoReply(selectedItem);
        controller.setRecipientstoReply();

        Scene newScene = new Scene(newSceneRoot, 450 , 500);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Reply");
        newStage.show();
    }

    @FXML
    private void ReplyAll() throws IOException {
        Email selectedItem = emailRlist.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/ReplyAll.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerReplyAll controller = loader.getController();
        controller.setEmailtoReply(selectedItem);
        controller.setRecipientstoReply();

        Scene newScene = new Scene(newSceneRoot, 450, 500);
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Reply All");
        newStage.show();
    }

    //TODO GESTIONE ELIMINA

    //TODO GESTIONE RIDIMENSIONE FINESTRA

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListView(emailRlist);
        setListView(emailSlist);
    }

    Email currentEmail;

    public void setListView(ListView<Email> email){

        email.getItems().addAll(mailBox.getrEmails());
        email.setCellFactory(lv -> new ListCell<Email>() {
            @Override
            protected void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);
                setText(empty || email == null ? "" : email.getSubject());
            }
        });
        email.getSelectionModel().selectedItemProperty().addListener((observableValue, oldEmail, newEmail) -> {
            if (newEmail != null) {
                currentEmail = newEmail;
                Oggetto.setText(currentEmail.getSubject());
                Mittente.setText(currentEmail.getSender());
                Testo.setText(currentEmail.getBody());
                Data.setText(currentEmail.getTime());
                Destinatari.setText(currentEmail.getRecipientsString());
            }
        });
    }
}