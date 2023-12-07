package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerList implements Initializable {
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
    private Label Introduction;
    @FXML
    private Label SenderText;
    @FXML
    private Label DataText;
    @FXML
    private Label RecipientsText;
    @FXML
    private ListView<Email> emailRlist;
    @FXML
    private ListView<Email> emailSlist;
    private Email currentEmail;
    private ClientModel model;
    private ListProperty<Email> receivedEmails = new SimpleListProperty<>();
    private ListProperty<Email> sentEmails = new SimpleListProperty<>();
    final StringProperty emailAddress = new SimpleStringProperty();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void handleClose() {
        boolean close = false;
        try {
            close = startSocket(3);
         if (close)
         System.exit(0);
         else
             System.out.println("Errore");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean startSocket(int op) throws IOException {
        switch (op) {
            case 3:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName, 8080);
                    UserOperations left = new UserOperations(3, User.getText());
                    left.sendLeftRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = left.receiveLoginAuthentication(socketManager.getObjectInputStream());
                    if (response.getMessage().equals("Error, user not joined")) {
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
        }
        return true;
    }
    @FXML
    private void Remove(){
        model.remove(currentEmail);
    }
    @FXML
    private void WriteEmail() throws IOException {
        initializeNewScene("WriteEmail.fxml");
    }

    @FXML
    private void Forward() throws IOException {
        initializeNewScene("Forward.fxml");
    }
    @FXML
    private void Reply() throws IOException {
        initializeNewScene("Reply.fxml");
    }
    @FXML
    private void ReplyAll() throws IOException {
        initializeNewScene("ReplyAll.fxml");
    }

    private void initializeNewScene(String fxmlToLoad) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/example/usergui_v1/" + fxmlToLoad)));
            Parent newSceneRoot = loader.load();
            switch (fxmlToLoad){
                case "ReplyAll.fxml": {
                    ControllerReplyAll controller = loader.getController();
                    controller.initialize(currentEmail, emailAddress.get(), model);
                    break;
                }
                case "Reply.fxml":{
                    ControllerReply controller = loader.getController();
                    controller.initialize(currentEmail, emailAddress.get(), model);
                    break;
                }
                case "Forward.fxml": {
                    ControllerForward controller = loader.getController();
                    controller.initialize(currentEmail, emailAddress.get(), model);
                    break;
                }
                case "WriteEmail.fxml": {
                    ControllerWriteMail controller = loader.getController();
                    controller.initialize(emailAddress.get(), model);
                    break;
                }
            }
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

            newScene.setFill(Color.TRANSPARENT);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: white; -fx-border-color: #e3dddd; -fx-border-radius: 10px");
            newStage.show();

        } catch (NullPointerException e) {
            System.out.println("The file doesn't exists" + e);
        }
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
        receivedEmails.bind(model.rEmailsProperty());
        sentEmails.bind(model.sEmailsProperty());
        emailAddress.bind(model.mailBoxOwnerProperty());
    }

    private void setListView(ListView<Email> email,boolean received){
        if(received) {
            email.getItems().addAll(receivedEmails.get());
        }
        else{
            email.getItems().addAll(sentEmails.get());
        }
        email.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);
                setText(empty || email == null ? "" : email.getSubject());
            }
        });
        email.getSelectionModel().selectedItemProperty().addListener((observableValue, oldEmail, newEmail) -> {
            if (newEmail != null) {
                if(received) {
                    emailSlist.getSelectionModel().clearSelection();
                }
                else{
                    emailRlist.getSelectionModel().clearSelection();
                }
                Introduction.setText("");
                SenderText.setText("Sender : ");
                DataText.setText("Data : ");
                RecipientsText.setText("Recipients :");
                currentEmail = newEmail;
                Subject.setText(currentEmail.getSubject());
                Sender.setText(currentEmail.getSender());
                Body.setText(currentEmail.getBody());
                Data.setText(currentEmail.getTime());
                Recipients.setText(currentEmail.getRecipientsString());
            }
        });
    }

    @FXML
    private void handleResize(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    protected void setUsers(String username)
    {
        User.setText(username);
    }
}