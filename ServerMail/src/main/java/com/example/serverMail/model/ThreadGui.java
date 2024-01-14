package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;
import javafx.application.Platform;
import java.util.List;

public class ThreadGui implements Runnable {
    private final MailServerController controller;
    private final String username;
    String currentTime;
    Operation op;
    SendType sendType;
    List<String> recipients;

    public ThreadGui(MailServerController controller, String username, String currentTime, Operation op) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
    }

    public ThreadGui(MailServerController controller, String username, String currentTime, Operation op,
            SendType sendType, List<String> recipients) {
        this.currentTime = currentTime;
        this.controller = controller;
        this.username = username;
        this.op = op;
        this.sendType = sendType;
        this.recipients = recipients;
    }

    @Override
    public void run() {
        switch (op) {
            case LOGIN:
                Platform.runLater(() -> controller.addLogMessage(username + " has joined " + currentTime));
                System.out.println("Join");
                break;
            case EXIT:
                Platform.runLater(() -> controller.addLogMessage(username + " has left " + currentTime));
                System.out.println("Left");
                break;
            case SEND:
                String finalResult = recipientsString();
                final String finalLogMessage = getLogMessage();
                Platform.runLater(() -> controller.addLogMessage(username + finalLogMessage + finalResult + " at " + currentTime));

                System.out.println(sendType.name().toLowerCase()); // Esempio: "send", "forward", "reply", "replyall"
                break;
            case DELETE:
                Platform.runLater(() -> controller.addLogMessage(username + " has delete one message at " + currentTime));
                break;
            case ERROR:
                String werongRecipients = recipientsString();
                Platform.runLater(() -> controller.addLogMessage(
                        username + " has tried to sent an email to " + werongRecipients + " at " + currentTime));
                break;
            case RECEIVE:
                Platform.runLater(() -> controller.addLogMessage(username + " has received new email at " + currentTime));
                break;
            default:
                break;
        }

    }

    private String getLogMessage() {
        String logMessage = "";

        if (sendType == SendType.SEND) {
            logMessage = " has sent an email to ";
        } else if (sendType == SendType.FORWARD) {
            logMessage = " has forwarded an email to ";
        } else if (sendType == SendType.REPLY) {
            logMessage = " has replied to an email to ";
        } else if (sendType == SendType.REPLYALL) {
            logMessage = " has replied all to an email to ";
        }

        return logMessage;
    }

    public String recipientsString() {
        String result;
        if (recipients.size() < 2) {
            StringBuilder resultBuilder = new StringBuilder();
            for (String s : recipients)
                resultBuilder.append(s).append(" ");
            result = resultBuilder.toString();
        } else {
            int length = recipients.size();
            StringBuilder resultBuilder = new StringBuilder();
            for (String s : recipients) {
                if (length > 2) {
                    resultBuilder.append(resultBuilder).append(", ");
                    length--;
                } else if (length == 2) {
                    resultBuilder.append(s).append(" and ");
                    length--;
                } else
                    resultBuilder.append(s);
            }
            result = resultBuilder.toString();
        }
        return result;
    }
}
