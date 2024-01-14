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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerList implements Initializable {
    public Pane contentPane;
    @FXML
    private VBox vbox;
    @FXML
    private Label User;
    @FXML
    private Label Recipients;
    @FXML
    private Label Body;
    @FXML
    private Label Subject;
    @FXML
    private Label Sender;
    @FXML
    private Label SenderText;
    @FXML
    private Label Data;
    @FXML
    private Label Introduction;
    @FXML
    private Label DataText;
    @FXML
    private Label RecipientsText;
    @FXML
    private AnchorPane clientRoot;
    @FXML
    private ListView<Email> emailRlist;
    @FXML
    private ListView<Email> emailSlist;
    @FXML
    private Button delete;
    @FXML
    private Button forward;
    @FXML
    private Button reply;
    @FXML
    private Button replyAll;
    @FXML
    private HBox hbox;

    private double originalLabelSize;
    private double originalVboxSize;
    private double originalButtonSize;
    double fontSize = Double.parseDouble("12px".replaceAll("[^\\d.]", ""));

    private Email currentEmail;
    private ClientModel model;
    private boolean typeEmail;
    ControllerPopUp popUp = new ControllerPopUp();
    private final SocketManager socket = new SocketManager();
    private double xOffset = 0;
    private double yOffset = 0;
    private MailBox mailBox;

    private int count = 0;


    @FXML
    private void handleClose() {
        try {
            System.out.println("Close start");
            socket.startSocket(Operation.EXIT);
            System.out.println("Close request");
            System.exit(0);
        } catch (RuntimeException | IOException e) {
            System.out.println("Logout failed, server not connected : " + e);
            System.exit(0);
        }
    }

    @FXML
    private void Remove() {
        try{
            if(currentEmail == null) {
                popUp.startPopUp("NullEmail", false);
            }
            else{
                socket.setType(typeEmail);
                boolean remove = socket.setEmailToDelete(currentEmail);
                //clean view client
                if(remove) {
                    if(typeEmail) {
                        emailRlist.getSelectionModel().clearSelection();
                        emailRlist.getItems().remove(currentEmail);
                    }
                    else {
                        emailSlist.getSelectionModel().clearSelection();
                        emailSlist.getItems().remove(currentEmail);
                    }
                    currentEmail = null;
                    Introduction.setText("Email deleted");
                    SenderText.setText("");
                    DataText.setText("");
                    RecipientsText.setText("");
                    Subject.setText("");
                    Sender.setText("");
                    Body.setText("");
                    Data.setText("");
                    Recipients.setText("");
                }}}
        catch (RuntimeException e){
            System.out.println("There was an error while deleting an email" + e);
        }

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
            switch (fxmlToLoad) {
                case "ReplyAll.fxml": {
                    ControllerReplyAll controller = loader.getController();
                    controller.initialize(currentEmail, User.getText());
                    break;
                }
                case "Reply.fxml": {
                    ControllerReply controller = loader.getController();
                    controller.initialize(currentEmail, User.getText());
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

    protected boolean setListView(ListView<Email> email, boolean received) {
        try {
            List<Email> newEmails = received ? mailBox.getrEmails() : mailBox.getsEmails();
            if (newEmails == null || newEmails.isEmpty()) {
                return false;
            }
            email.getItems().addAll(0, newEmails);

            email.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Email email, boolean empty) {
                    super.updateItem(email, empty);
                    setText(empty || email == null ? "" : email.getSubject());
                }
            });
            email.getSelectionModel().selectedItemProperty().addListener((observableValue, oldEmail, newEmail) -> {
                if (newEmail != null) {
                    if (received) {
                        typeEmail = true;
                        emailSlist.getSelectionModel().clearSelection();
                    } else {
                        typeEmail = false;
                        emailRlist.getSelectionModel().clearSelection();
                    }
                    Introduction.setText("");
                    SenderText.setText("  Sender : ");
                    DataText.setText("  Data : ");
                    RecipientsText.setText("  Recipients :");
                    currentEmail = newEmail;
                    Subject.setText(" " + currentEmail.getSubject());
                    Sender.setText(currentEmail.getSender());
                    Body.setText(currentEmail.getBody());
                    Data.setText(currentEmail.getTime());
                    Recipients.setText(currentEmail.getRecipientsString());
                }
            });
            return true;
        } catch (NullPointerException e) {
            System.out.println("There was a problem while setting the list view" + e);
        }
        return false;
    }

    @FXML
    private void handleResize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());

        if (stage.isMaximized()) {
            // Store the original size
            originalLabelSize = fontSize;
            originalVboxSize = vbox.getWidth();
            originalButtonSize = replyAll.getWidth();
            // Increase the size
            double newSizeButton = originalButtonSize * 2.5;
            double newSizeText = originalLabelSize + 10;
            double newSizeVbox = originalVboxSize * 2.5;

            hbox.setPrefWidth(newSizeVbox);
            delete.setPrefWidth(newSizeButton);
            forward.setPrefWidth(newSizeButton);
            reply.setPrefWidth(newSizeButton);
            replyAll.setPrefWidth(newSizeButton);

            delete.setFont(new javafx.scene.text.Font(newSizeText));
            forward.setFont(new javafx.scene.text.Font(newSizeText));
            reply.setFont(new javafx.scene.text.Font(newSizeText));
            replyAll.setFont(new javafx.scene.text.Font(newSizeText));

            SenderText.setFont(new javafx.scene.text.Font(newSizeText));
            DataText.setFont(new javafx.scene.text.Font(newSizeText));
            RecipientsText.setFont(new javafx.scene.text.Font(newSizeText));
            Subject.setFont(new javafx.scene.text.Font(newSizeText));
            Body.setFont(new javafx.scene.text.Font(newSizeText));

            Sender.setFont(new Font(newSizeText));
            Data.setFont(new Font(newSizeText));
            Recipients.setFont(new Font(newSizeText));


        } else {
            // Restore the original size
            hbox.setPrefWidth(originalVboxSize);
            delete.setPrefWidth(originalButtonSize);
            forward.setPrefWidth(originalButtonSize);
            reply.setPrefWidth(originalButtonSize);
            replyAll.setPrefWidth(originalButtonSize);

            delete.setFont(new javafx.scene.text.Font(originalLabelSize));
            forward.setFont(new javafx.scene.text.Font(originalLabelSize));
            reply.setFont(new javafx.scene.text.Font(originalLabelSize));
            replyAll.setFont(new javafx.scene.text.Font(originalLabelSize));

            SenderText.setFont(new javafx.scene.text.Font(originalLabelSize));
            DataText.setFont(new javafx.scene.text.Font(originalLabelSize));
            RecipientsText.setFont(new javafx.scene.text.Font(originalLabelSize));
            Subject.setFont(new javafx.scene.text.Font(originalLabelSize));
            Body.setFont(new javafx.scene.text.Font(originalLabelSize));

            Sender.setFont(new javafx.scene.text.Font(originalLabelSize));
            Data.setFont(new javafx.scene.text.Font(originalLabelSize));
            Recipients.setFont(new javafx.scene.text.Font(originalLabelSize));

        }
    }


    protected void setUsers(String username) {
        User.setText(username);
        socket.setUsername(username);
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
        setListView(emailSlist, false);
        if (setListView(emailRlist, true) && count > 0) {
            Stage parentStage = (Stage) clientRoot.getScene().getWindow();
            double newX = parentStage.getX() + 5;
            double newY = parentStage.getY() + 380;
            popUp.setPosition(newX, newY);
            popUp.startPopUp("NewMailArrived", true);
        }
        count++;
    }
}


