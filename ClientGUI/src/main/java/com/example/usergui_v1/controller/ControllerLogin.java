package com.example.usergui_v1.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.usergui_v1.model.*;
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
        boolean result = startSocket(Operation.REGISTER);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/usergui_v1/Client.fxml"));
            Parent newSceneRoot = loader.load();
            Scene newScene = new Scene(newSceneRoot);
            ControllerList temp = loader.getController();
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

        boolean loginAuthorized = startSocket(Operation.LOGIN);
            if(loginAuthorized) {
                closeLoginWindow();
                temp.setUsers(username.getText());
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
        public boolean startSocket(Operation LogReg) {
        switch (LogReg) {
            case LOGIN:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations askAuthentication = new UserOperations(Operation.LOGIN, username.getText());
                    askAuthentication.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = askAuthentication.receiveServerResponse(socketManager.getObjectInputStream());
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
            case REGISTER:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations register = new UserOperations(Operation.REGISTER, new MailBox(new ArrayList<Email>(), new ArrayList<Email>(), username.getText()));
                    register.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = register.receiveServerResponse(socketManager.getObjectInputStream());
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
//            case SEND:
//                //////////////////
//                List<String> recipients = new ArrayList<>();
//                recipients.add(username.getText());
//                Email email = new Email("Bilguun@gmail.com",recipients, "test", "test123123", new Date(), "134223");
//                UserOperations userOperations = new UserOperations(Operation.SEND, username.getText(), email, null, null, false, null);
//                SocketManager socketManager = new SocketManager()
//                userOperations.sendRequest();
//                //////////////////
//            break;
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
