package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;

public class ThreadGui implements Runnable{
    private MailServerController controller;
    private String username;
    public  ThreadGui(MailServerController controller, String username)
    {
        this.controller = controller;
        this.username = username;
    }
    @Override
    public void run()
    {
        Platform.runLater(() -> {
            controller.addLogMessageLogin(username + " ha appena fatto l'accesso ");
        });

        //controller.addLogMessageLogin(username + " ha appena fatto l'accesso ");
    }
}
