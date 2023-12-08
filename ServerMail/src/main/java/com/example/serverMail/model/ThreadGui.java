package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;

public class ThreadGui implements Runnable{
    private MailServerController controller;
    private String username;
    String currentTime;
    int op;
    public  ThreadGui(MailServerController controller, String username, String currentTime, int op) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
    }
    @Override
    public void run()
    {
        switch (this.op)
        {
            case 0:
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has joined " + currentTime);
                });
                System.out.println("Join");
                break;
            case 1:
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has left " + currentTime);
                });
                System.out.println("Left");
                break;
        }


        //controller.addLogMessageLogin(username + " ha appena fatto l'accesso ");
    }
}
