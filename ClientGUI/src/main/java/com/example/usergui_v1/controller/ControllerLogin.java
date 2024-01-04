package com.example.usergui_v1.controller;

import java.io.IOException;

import com.example.usergui_v1.model.*;
import javafx.application.Platform;
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
    private Text feedback;
    private double xOffset = 0;
    private double yOffset = 0;
    private final SocketManager socket = new SocketManager();

    public ControllerLogin() {}

    @FXML
    private void register() {
       try {
           //ControllerPopUp popUp = new ControllerPopUp();
           socket.setUsername(username.getText());
           boolean result = socket.startSocket(Operation.REGISTER);
           if (result) {
               // popUp.startPopUp("SingUp", true);
               feedback.setText("Registration Successful! Now you can log in.");
               feedback.setFill(Color.GREEN);
           } else {
               feedback.setText("Please write in the form of username@example.com");
               feedback.setFill(Color.RED);
           }
       }
       catch (NullPointerException e){
           System.out.println("Registration Failed");
       }
    }
    @FXML
    private void exit() {
        System.exit(0);
    }
    @FXML
    private void login() throws IOException {
        try {
            socket.setUsername(username.getText());
            boolean loginAuthorized = socket.startSocket(Operation.LOGIN);
            if(loginAuthorized) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/usergui_v1/Client.fxml"));
                Parent newSceneRoot = loader.load();
                Scene newScene = new Scene(newSceneRoot);
                ControllerList temp = loader.getController();
                
                //This thread is used to update the mailbox every 10 seconds
                new Thread(() -> {
                    MailBox current = socket.getMailbox();
                    System.out.println(current);
                    temp.setMailBox(current);
                    while (true) {
                        try {
                            synchronized (socket) {
                                socket.wait(6000);
                                MailBox updated = socket.getMailbox();
                                if (!current.equals(updated)) {
                                    System.out.println(updated);
                                    current = updated;
                                    MailBox finalCurrent = current;
                                    Platform.runLater(() -> temp.setMailBox(finalCurrent));
                                }
                            }

                        } catch (InterruptedException e) {
                            System.out.println("Error in update thread " +e);
                        }
                    }
                }).start();


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
                newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: white; -fx-border-color: #e3dddd;");
                newStage.show();
            } else {
                errorMessage.setText("Wrong credentials, try again");
            }

        } catch (NullPointerException e) {
            System.out.println("The file doesn't exist" + e);
        }
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
}
