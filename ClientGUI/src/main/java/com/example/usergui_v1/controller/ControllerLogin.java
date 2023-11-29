package com.example.usergui_v1.controller;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ControllerLogin {
    @FXML
    private BorderPane loginRoot;

    public ControllerLogin() {}

    @FXML
    private void exit(MouseEvent event) {
        System.exit(0);
    }
    @FXML
    private void login() throws IOException {
        try {
            // Load the new scene
            Parent newSceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Client.fxml")));
            Scene newScene = new Scene(newSceneRoot);

            // Create a new stage for the new scene
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
            closeLoginWindow();
        } catch (NullPointerException e) {
            System.out.println("The file doesn't exist" + e);
        }
    }
    public void closeLoginWindow() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }
}
