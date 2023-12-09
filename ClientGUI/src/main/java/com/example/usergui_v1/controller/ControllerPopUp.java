package com.example.usergui_v1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class ControllerPopUp {

    @FXML
    private AnchorPane popupRoot;
    @FXML
    private Label ErrorPopUp;

    @FXML
    private void Close() {
        Stage stage = (Stage) popupRoot.getScene().getWindow();
        stage.close();
    }

    public void initialize(String ErrorType) {
        if(Objects.equals(ErrorType, "ReplySent")){
            ErrorPopUp.setText("Impossible to reply, to an email sent.");
        }
        if(Objects.equals(ErrorType, "FewArguments")){
            ErrorPopUp.setText("Impossible to sent Email due to void field.");
        }
        if(Objects.equals(ErrorType, "ServerConnection")){
            ErrorPopUp.setText("Impossible to connect to server right now. Try again later.");
        }
        if(Objects.equals(ErrorType, "NullEmail")){
            ErrorPopUp.setText("No Email selected. Select one and try again.");
        }
        if(Objects.equals(ErrorType, "WrongFormatEmail")){
            ErrorPopUp.setText("Please write email in form of username@example.com/.it ");
        }
        if(Objects.equals(ErrorType, "Access denied")){
            ErrorPopUp.setText("Access denied. Wrong password or email.");
        }


    }
}
