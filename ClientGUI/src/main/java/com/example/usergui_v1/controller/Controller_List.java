package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.ClientModel;
import com.example.usergui_v1.model.Email;
import com.example.usergui_v1.model.MailBox;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller_List implements Initializable {
    @FXML
    private Label User;
    @FXML
    private Label Recipients;
    @FXML
    private Label Subject;
    @FXML
    private Label Body;
    @FXML
    private Label Sender;
    @FXML
    private Label Data;
    @FXML
    private ListView<Email> emailRlist;
    @FXML
    private ListView<Email> emailSlist;
    Email currentEmail;
    private ClientModel model;
    private ListProperty<Email> receivedEmails = new SimpleListProperty<>();
    private ListProperty<Email> sentEmails = new SimpleListProperty<>();
    private final StringProperty emailAddress = new SimpleStringProperty();

    private double xOffset = 0;
    private double yOffset = 0;



    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void Remove() throws IOException{
        model.remove(currentEmail);
    }
    @FXML
    private void WriteEmail() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/WriteEmail.fxml")));
            Parent newSceneRoot = loader.load();
            ControllerWriteMail controller = loader.getController();
            controller.initialize(emailAddress.get(),model);

            Scene newScene = new Scene(newSceneRoot);
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

            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();

        } catch (NullPointerException e) {
            System.out.println("The file doesn't exists" + e);
        }
    }

    @FXML
    private void Forward() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Forward.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerForward controller = loader.getController();
        controller.initialize(currentEmail,emailAddress.get(),model);

        Scene newScene = new Scene(newSceneRoot, 450 , 150);
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

        newStage.initStyle(StageStyle.UNDECORATED);

        newStage.show();
    }

    @FXML
    private void Reply() throws IOException {
        Email selectedItem = emailRlist.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/Reply.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerReply controller = loader.getController();
        controller.initialize(currentEmail,emailAddress.get(),model);

        Scene newScene = new Scene(newSceneRoot);
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

        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.show();
    }

    @FXML
    private void ReplyAll() throws IOException {
        Email selectedItem = emailRlist.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/ReplyAll.fxml")));
        Parent newSceneRoot = loader.load();
        ControllerReplyAll controller = loader.getController();
        controller.initialize(currentEmail,emailAddress.get(),model);

        Scene newScene = new Scene(newSceneRoot);
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

        newStage.initStyle(StageStyle.UNDECORATED);

        newStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new ClientModel();
        initializeBindings();
        User.setText("Client :  " + emailAddress.get());
        setListView(emailRlist,true);
        setListView(emailSlist,false);
    }

    private void initializeBindings() {
        MailBox mailBox = model.getMailBox();

        // Bindings unidirezionali
        receivedEmails.bind(model.rEmailsProperty());
        sentEmails.bind(model.sEmailsProperty());
        emailAddress.bind(model.mailBoxOwnerProperty());
    }

    public void setListView(ListView<Email> email,boolean received){
        if(received) {
            email.getItems().addAll(receivedEmails.get());
        }
        else{
            email.getItems().addAll(sentEmails.get());
        }
        email.setCellFactory(lv -> new ListCell<Email>() {
            @Override
            protected void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);
                setText(empty || email == null ? "" : email.getSubject());
            }
        });
        email.getSelectionModel().selectedItemProperty().addListener((observableValue, oldEmail, newEmail) -> {
            if (newEmail != null) {
                currentEmail = newEmail;
                Subject.setText(currentEmail.getSubject());
                Sender.setText(currentEmail.getSender());
                Body.setText(currentEmail.getBody());
                Data.setText(currentEmail.getTime());
                Recipients.setText(currentEmail.getRecipientsString());
            }
        });
    }
}