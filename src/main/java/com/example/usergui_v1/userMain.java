package com.example.usergui_v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class userMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(userMain.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 450);
        stage.setTitle("Email application!");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}