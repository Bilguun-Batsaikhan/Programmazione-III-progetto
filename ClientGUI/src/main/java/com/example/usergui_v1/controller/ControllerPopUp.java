package com.example.usergui_v1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


public class ControllerPopUp {

    @FXML
    private AnchorPane popupRoot;
    @FXML
    private Label errorPopUp;
    private double xOffset, yOffset;
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
            case "Registration Successful! Now you can log in.":
                errorPopUp.setText(errorType);
                break;
            default:
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
}
