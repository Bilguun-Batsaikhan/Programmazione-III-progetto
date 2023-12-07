package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;

public class ThreadGui implements Runnable{
    private MailServerController controller;
    private String username;
    String currentTime;
    public  ThreadGui(MailServerController controller, String username, String currentTime) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
    }
    @Override
    public void run()
    {
        Platform.runLater(() -> {
            controller.addLogMessageLogin(username + " has joined " + currentTime);
        });

        //controller.addLogMessageLogin(username + " ha appena fatto l'accesso ");
    }
}
