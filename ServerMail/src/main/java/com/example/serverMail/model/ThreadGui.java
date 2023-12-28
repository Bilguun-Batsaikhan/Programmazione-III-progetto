package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;
import java.util.List;

public class ThreadGui implements Runnable{
    private MailServerController controller;
    private String username;
    String currentTime;
    Operation op;
    SendType sendType;
    List<String> recipients;

    public  ThreadGui(MailServerController controller, String username, String currentTime,Operation op) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
    }

    public ThreadGui(MailServerController controller, String username, String currentTime, Operation op,SendType sendType, List<String> recipients)
    {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
        this.sendType = sendType;
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
                switch (sendType){
                    case SEND:
                        Platform.runLater(() -> {

                            controller.addLogMessageLogin(username + " has sent an email to " + finalResult + " at " + currentTime);
                        });
                        System.out.println("send");
                        break;
                    case FOWARD:
                        Platform.runLater(() -> {

                            controller.addLogMessageLogin(username + " has forward an email to " + finalResult + " at " + currentTime);
                        });
                        System.out.println("forward");
                        break;
                    case REPLY:
                        Platform.runLater(() -> {

                            controller.addLogMessageLogin(username + " has reply an email to " + finalResult + " at " + currentTime);
                        });
                        System.out.println("reply");
                        break;
                    case REPLYALL:
                        Platform.runLater(() -> {

                            controller.addLogMessageLogin(username + " has reply all an email to " + finalResult + " at " + currentTime);
                        });
                        System.out.println("send");
                        break;
                }
                break;
            case DELETE:
                Platform.runLater(() -> {
                    controller.addLogMessageLogin(username + " has delete one message at " + currentTime);
                });
        }

    }
}
