package com.example.usergui_v1.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import com.example.usergui_v1.model.ServerResponse;
import com.example.usergui_v1.model.UserOperations;
import com.example.usergui_v1.model.SocketManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerLogin {
    @FXML
    private Text errorMessage;
    @FXML
    private BorderPane loginRoot;
    @FXML
    private TextField username;
    @FXML
    private Text register;
    private double xOffset = 0;
    private double yOffset = 0;

    public ControllerLogin() {}

    @FXML
    private void register() {
        boolean result = startSocket(2);
        if(result) {
            register.setText("Now you can login!");
        } else {
            register.setText("Please write in form of username@example.com");
        }
    }
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

            boolean loginAuthorized = startSocket(1);
            if(loginAuthorized) {
                closeLoginWindow();
                newScene.setFill(Color.TRANSPARENT);
                newStage.initStyle(StageStyle.TRANSPARENT);
                newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: white;");
                newStage.show();
            } else {
                errorMessage.setText("Access denied!!!");
            }

        } catch (NullPointerException e) {
            System.out.println("The file doesn't exist" + e);
        }
    }
        public boolean startSocket(int LogReg) {
        switch (LogReg) {
            case 1:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations askAuthentication = new UserOperations(1, username.getText());
                    askAuthentication.sendLoginRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = askAuthentication.receiveLoginAuthentication(socketManager.getObjectInputStream());
                    if(response.getMessage().equals("Access denied")) {
                        System.out.println(response.getMessage());
                        return false;
                    }
                    socketManager.closeConnection();
                } catch (UnknownHostException e) {
                    System.out.println("Login failed " + e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            break;
            case 2:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations register = new UserOperations(2, username.getText());
                    register.sendLoginRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = register.receiveLoginAuthentication(socketManager.getObjectInputStream());
                    if(response.getMessage().equals("Access denied")) {
                        System.out.println(response.getMessage());
                        return false;
                    } else {
                        return true;
                    }
                } catch (UnknownHostException e) {
                    System.out.println("Registration failed " + e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            break;
        }
            return true;
        }
    public void closeLoginWindow() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleResize(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    private void startPopUp(String error) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/PopUpWarning.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerPopUp controller = loader.getController();
        controller.initialize(error);

        Scene newScene = new Scene(newSceneRoot);
        Stage newStage = new Stage();
        newStage.setScene(newScene);

        newScene.setFill(Color.TRANSPARENT);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: #ffc400;");
        newStage.showAndWait();
    }


}
