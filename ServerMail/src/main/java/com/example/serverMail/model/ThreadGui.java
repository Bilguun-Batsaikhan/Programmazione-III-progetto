package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;
import java.util.List;

public class ThreadGui implements Runnable{
    private MailServerController controller;
    private String username;
    String currentTime;
    Operation op;
    List<String> recipients;
    public  ThreadGui(MailServerController controller, String username, String currentTime,Operation op) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
    }

    public ThreadGui(MailServerController controller, String username, String currentTime, Operation op, List<String> recipients)
    {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
        this.recipients= recipients;
    }
    @Override
    public void run()
    {
        switch (op)
        {
            case LOGIN:
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has joined " + currentTime);
                });
                System.out.println("Join");
                break;
            case EXIT:
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has left " + currentTime);
                });
                System.out.println("Left");
                break;
            case SEND:
                String result = "";
                if(recipients.size() < 2) {
                    for (String s : recipients)
                        result += s + " ";
                }
                else {
                    int length = recipients.size();
                    for (String s : recipients) {
                        if(length > 2)
                        {
                            result += result+ ", ";
                            length--;
                        }
                        else if (length == 2) {
                            result += s + " and ";
                            length--;
                        }
                        else
                            result += s;
                    }
                }
                String finalResult = result;
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has sent an email to " + finalResult + " at " +currentTime);
                });
                System.out.println("send");
                break;
        }

    }
}
