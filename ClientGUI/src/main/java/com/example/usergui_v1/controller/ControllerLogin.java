package com.example.usergui_v1.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import com.example.usergui_v1.model.SocketManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerLogin {
    @FXML
    private BorderPane loginRoot;
    @FXML
    private TextField username;
    private double xOffset = 0;
    private double yOffset = 0;

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

            newScene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            newScene.setOnMouseDragged(event -> {
                newStage.setX(event.getScreenX() - xOffset);
                newStage.setY(event.getScreenY() - yOffset);
            });
            startSocket();
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
            closeLoginWindow();
        } catch (NullPointerException e) {
            System.out.println("The file doesn't exist" + e);
        }
    }
    public void startSocket() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            SocketManager socketManager = new SocketManager(hostName, this.username.getText(),8080);
            socketManager.sendRequest();
            String response = socketManager.receiveResponse();
            System.out.println(response);
            socketManager.closeConnection();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void closeLoginWindow() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }
}
