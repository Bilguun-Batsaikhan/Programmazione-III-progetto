package com.example.serverMail;
import com.example.serverMail.controller.*;
import com.example.serverMail.model.Server;
import com.example.serverMail.model.MailServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MailServerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server-view.fxml"));
        MailServerController controller = new MailServerController();

        Scene scene = new Scene(loader.load(), 800, 450);// Ora puoi chiamare il metodo init() dopo aver caricato il controller


        int port = 8080; // You can change this to the desired port
        Server server = new Server(port);
        server.start();

        stage.setTitle("Mail Server");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
