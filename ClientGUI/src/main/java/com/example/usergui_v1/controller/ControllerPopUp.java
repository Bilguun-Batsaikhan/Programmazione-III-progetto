package com.example.usergui_v1.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;


public class ControllerPopUp {

    @FXML
    private AnchorPane popupRoot;
    @FXML
    private Label errorPopUp;
    private double xOffset, yOffset;

    private double x = 0;
    private double y = 0;

    @FXML
    private void Close() {
        Stage stage = (Stage) popupRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(String errorType) {
        makeSceneDraggable();
        switch (errorType) {
            case "ReplySent":
                errorPopUp.setText("Impossible to reply to an email sent.");
                break;
            case "SameSender":
                errorPopUp.setText("Impossible to send to you an email from you");
                break;
            case "FewArguments":
                errorPopUp.setText("Impossible to send email due to void field.");
                break;
            case "ServerConnection":
                errorPopUp.setText("Impossible to connect to the server right now. Try again later.");
                break;
            case "NullEmail":
                errorPopUp.setText("No email selected. Select one and try again.");
                break;
            case "WrongFormatEmail":
                errorPopUp.setText("Please write email in the form of username@example.com/.it");
                break;
            case "AccessDenied":
                errorPopUp.setText("Access denied. Wrong password or email.");
                break;
            case "MailSent":
                errorPopUp.setText("Mail sent successfully!");
                break;
            case "EmailNotExist":
                errorPopUp.setText("Impossible to send! Email address does not exist or sender email is equals to receiver email");
                break;
            case "NewMailArrived":
                errorPopUp.setText("New emails has arrived!");
                break;
            default:
                errorPopUp.setText("Unexpected Error");
                // Handle unexpected errorType
                System.err.println("Unexpected ErrorType: " + errorType);
                break;
        }
    }

    private void makeSceneDraggable() {
        popupRoot.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        popupRoot.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) popupRoot.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    public void startPopUp(String error, boolean success) {
        try {
            FXMLLoader loader;
        if(success){
            loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/PopUpSuccess.fxml")));
        }
        else {
            loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/PopUpWarning.fxml")));
        }
        Parent newSceneRoot = loader.load();
        ControllerPopUp controller = loader.getController();
        controller.initialize(error);

        Scene newScene = new Scene(newSceneRoot);
        Stage newStage = new Stage();
        newStage.setScene(newScene);

        newScene.setFill(Color.TRANSPARENT);
        newStage.initStyle(StageStyle.TRANSPARENT);
        if(success) {
            newStage.setX(x);
            newStage.setY(y);
            newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-border-color: #e3dddd; -fx-background-color: #ffffff; -fx-border-radius: 10px");
        }
        else{
            newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: #ffc400;");
        }
        if(success) {
                PauseTransition delay = new PauseTransition(Duration.seconds(6));
                delay.setOnFinished(event -> newStage.close());

                delay.play();
        }
        newStage.showAndWait();
    } catch (IOException | RuntimeException e) {
            System.out.println("There is a problem while starting the pop up" + e);
        }
    }


    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
