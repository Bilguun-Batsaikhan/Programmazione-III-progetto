module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.serverMail to javafx.fxml;
    exports com.example.serverMail;
    exports com.example.serverMail.controller;
    opens com.example.serverMail.controller to javafx.fxml;
    opens com.example.serverMail.model to com.google.gson;
}