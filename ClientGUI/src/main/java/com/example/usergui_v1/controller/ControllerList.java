package com.example.usergui_v1.controller;

import com.example.usergui_v1.model.*;
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
import java.net.URL;
import java.util.List;
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

    private final SocketManager socket = new SocketManager();
    private double xOffset = 0;
    private double yOffset = 0;
    private MailBox mailBox;

    @FXML
    private void handleClose() {
        try {
            System.out.println("Close start");
            socket.setUsername(User.getText());
            socket.startSocket(Operation.EXIT);
            System.out.println("Close request");
            System.exit(0);
        }
        catch (RuntimeException e){
            System.out.println("Logout failed, server not connected : " + e);
            System.exit(0);
        }
    }
    @FXML
    private void Refresh() {
        try{
            socket.setUsername(User.getText());
            this.mailBox = socket.getMailbox();
            ControllerPopUp popUp = new ControllerPopUp();
            if(setListView(emailRlist,true)){
                popUp.startPopUp("NewMailArrived",true);
            }
            setListView(emailSlist,false);
        }
        catch (NullPointerException | IOException e){
            System.out.println("There is a problem while refreshing " + e);
        }
    }
    @FXML
    private void Remove(){
        socket.setEmailToDelete(currentEmail);
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
                    controller.initialize(currentEmail, User.getText(), model);
                    break;
                }
                case "Reply.fxml":{
                    ControllerReply controller = loader.getController();
                    controller.initialize(currentEmail, User.getText(), model);
                    break;
                }
                case "Forward.fxml": {
                    ControllerForward controller = loader.getController();
                    controller.initialize(currentEmail, User.getText(), model);
                    break;
                }
                case "WriteEmail.fxml": {
                    ControllerWriteMail controller = loader.getController();
                    controller.initialize(User.getText(), model);
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
            newSceneRoot.setStyle("-fx-background-radius: 10px; -fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #e3dddd");
            newStage.show();
        } catch (NullPointerException e) {
            System.out.println("The file doesn't exists" + e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new ClientModel();
    }

    protected boolean setListView(ListView<Email> email,boolean received) throws NullPointerException{
        List<Email> newEmails = received ? mailBox.getrEmails() : mailBox.getsEmails();
        if (newEmails.equals(email.getItems())) {
            return false;
        }
        email.getItems().clear();
        email.getItems().addAll(newEmails);

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
        return true;
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

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
        setListView(emailRlist,true);
        setListView(emailSlist,false);
    }
}

