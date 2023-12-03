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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
    private void exit() {
        System.exit(0);
    }
    @FXML
    private void login() throws IOException {
        try {
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
            boolean loginAuthorized = startSocket();
            if(loginAuthorized) {
                closeLoginWindow();
                newScene.setFill(Color.TRANSPARENT);
                newStage.initStyle(StageStyle.TRANSPARENT);
                newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: white;");
                newStage.show();
            }

        } catch (NullPointerException e) {
            System.out.println("The file doesn't exist" + e);
        }
    }
    public boolean startSocket() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            SocketManager socketManager = new SocketManager(hostName, this.username.getText(),8080);
            socketManager.sendRequest();
            try {
                String response = socketManager.receiveResponse();
                if(response.equals("Access denied")) {
                    System.out.println(response);
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e + "ERROR!");
            }
            socketManager.closeConnection();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void closeLoginWindow() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }
}
