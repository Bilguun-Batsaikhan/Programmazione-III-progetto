package com.example.serverMail;
import com.example.serverMail.controller.MailServerController;
import com.example.serverMail.model.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MailServerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server-view.fxml"));

        Scene scene = new Scene(loader.load(), 1000, 700);// Ora puoi chiamare il metodo init() dopo aver caricato il controller
        MailServerController controller = loader.getController();

        stage.setTitle("Mail Server");
        stage.setScene(scene);
        stage.show();

        // Start the server in a separate thread
        new Thread(() -> {
            int port = 8080;
            Server server = new Server(port, controller);
            server.start();
        }).start();
    }


    public static void main(String[] args) {
        launch();
    }
}
