package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.MailBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_List implements Initializable {
    @FXML
    private Label test;
    @FXML
    private ListView<String> emailRlist;
    @FXML
    private ListView<String> emailSlist;
    private final MailBox mailBox = new MailBox();

    @FXML
    private void WriteEmail() throws IOException {
        try {
            Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/WriteEmail.fxml")));
            Scene newScene = new Scene(newSceneRoot, 450 , 500);
            // Imposta la nuova scena in una nuova finestra (Stage)
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("New Email");
            newStage.show();
        } catch (NullPointerException e) {
            System.out.println("The file doesn't exists" + e);
        }
    }


    @FXML
    private void Inoltra() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Forward.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 100);
        // Imposta la nuova scena in una nuova finestra (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Forward");
        newStage.show();
    }

    @FXML
    private void Reply() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/NewScene.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 500);
        // Imposta la nuova scena in una nuova finestra (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Reply");
        newStage.show();
    }

    @FXML
    private void ReplyAll() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/NewScene.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 500);
        // Imposta la nuova scena in una nuova finestra (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Reply All");
        newStage.show();
    }
    String currentSemail;
    String currentRemail;
  
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailRlist.getItems().addAll(mailBox.getrSubjectsEmail());
        emailRlist.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            currentRemail = emailRlist.getSelectionModel().getSelectedItem();
            test.setText(currentRemail);
        });
        emailSlist.getItems().addAll(mailBox.getrSubjectsEmail());
        emailSlist.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            currentSemail = emailSlist.getSelectionModel().getSelectedItem();
            test.setText(currentSemail);
        });
    }
}