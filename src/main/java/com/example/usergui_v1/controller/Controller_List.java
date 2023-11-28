package com.example.usergui_v1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_List implements Initializable {
    @FXML
    private Label test;
    @FXML
    private ListView<String> emailList;

    @FXML
    private void WriteEmail() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/NewScene.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 500);
        // Imposta la nuova scena in una nuova finestra (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Nuova Mail");
        newStage.show();
    }


    @FXML
    private void Inoltra() throws IOException {
        Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Forward.fxml")));
        Scene newScene = new Scene(newSceneRoot, 450 , 100);
        // Imposta la nuova scena in una nuova finestra (Stage)
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setTitle("Inoltra");
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
    //Email.class
    String[] emailStrings = {"a", "b", "c", "d"};
    String[] contentsEmail = {"casa", "macchina", "albero", "cane", "gatto", "uccello", "fiore", "mare", "montagna"};
//    @FXML
//    protected void onHelloButtonClick() {
//
//    }
    String currentEmail;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailList.getItems().addAll(emailStrings);
        emailList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            currentEmail = emailList.getSelectionModel().getSelectedItem();
            test.setText(currentEmail);
        });
    }
}