module com.example.usergui_v1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.usergui_v1 to javafx.fxml;
    exports com.example.usergui_v1;
    exports com.example.usergui_v1.controller;
    opens com.example.usergui_v1.controller to javafx.fxml;
}