module com.example.usergui_v1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.base;


    opens com.example.usergui_v1 to javafx.fxml;
    exports com.example.usergui_v1;
    exports com.example.usergui_v1.controller;
    exports com.example.usergui_v1.model;
    opens com.example.usergui_v1.controller to javafx.fxml;

    opens com.example.usergui_v1.model to com.google.gson;
}