package com.example.usergui_v1.controller;

import java.io.IOException;

import com.example.usergui_v1.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Text feedback;
    @FXML
    private Button loginButton;
    @FXML
    private Text register;
    @FXML
    private Text welcome;

    private double originalSizeUser;
    private double originalSizeButton;
    private double originalSizeRegister;
    private double originalSizeWelcome;

    private double xOffset = 0;
    private double yOffset = 0;
    private final SocketManager socket = new SocketManager();

    public ControllerLogin() {
    }

    @FXML
    private void register() {
        try {
            // ControllerPopUp popUp = new ControllerPopUp();
            socket.setUsername(username.getText());
            boolean result = socket.startSocket(Operation.REGISTER);
            if (result) {
                // popUp.startPopUp("SingUp", true);
                feedback.setText("Registration Successful! Now you can log in.");
                feedback.setFill(Color.GREEN);
            } else {
                String response = socket.getResponseMessageRegister();
                if(response.equals("User already exists")) {
                    feedback.setText("You already have an account.");
                } else {
                    feedback.setText("Please write in the form of username@example.com");
                }
                feedback.setFill(Color.RED);
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Registration Failed");
        }
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private void login() {
        try {
            socket.setUsername(username.getText());
            boolean loginAuthorized = socket.startSocket(Operation.LOGIN);
            if (loginAuthorized) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/usergui_v1/Client.fxml"));
                Parent newSceneRoot = loader.load();
                Scene newScene = new Scene(newSceneRoot);
                ControllerList temp = loader.getController();

                socket.Refresh(temp, username.getText());

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
                closeLoginWindow();
                String user = username.getText();
                temp.setUsers(user);
                newScene.setFill(Color.TRANSPARENT);
                newStage.initStyle(StageStyle.TRANSPARENT);
                newSceneRoot.setStyle(
                        "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: white; -fx-border-color: #e3dddd;");
                newStage.show();
            } else {
                errorMessage.setText("Wrong credentials, try again");
            }

        } catch (NullPointerException | IOException e) {
            System.out.println("The file doesn't exist" + e);
        }
    }

    public void closeLoginWindow() {
        Stage stage = (Stage) loginRoot.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleResize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
        if (stage.isMaximized()) {
            // Store the original size
            originalSizeWelcome = welcome.getFont().getSize();
            originalSizeUser = username.getFont().getSize();
            originalSizeButton = loginButton.getWidth();
            originalSizeRegister = register.getFont().getSize();
            // Increase the size
            double newSizeUser = originalSizeUser + 5;
            double newSizeRegister = originalSizeRegister + 5;
            double newSizeWelcome = originalSizeWelcome + 5;
            double newSizeButton = originalSizeButton * 2.5;
            
            welcome.setFont(new javafx.scene.text.Font(newSizeWelcome));
            register.setFont(new javafx.scene.text.Font(newSizeRegister));
            username.setFont(new javafx.scene.text.Font(newSizeUser));
            loginButton.setPrefWidth(newSizeButton);
            loginButton.setFont(new javafx.scene.text.Font(newSizeUser));

        } else {
            // Restore the original size
            welcome.setFont(new javafx.scene.text.Font(originalSizeWelcome));
            register.setFont(new javafx.scene.text.Font(originalSizeRegister));
            username.setFont(new javafx.scene.text.Font(originalSizeUser));
            loginButton.setPrefWidth(originalSizeButton);
            loginButton.setFont(new javafx.scene.text.Font(originalSizeUser));
        }
    }
}
