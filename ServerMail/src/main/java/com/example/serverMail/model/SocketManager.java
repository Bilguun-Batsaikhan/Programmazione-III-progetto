package com.example.serverMail.model;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.example.serverMail.controller.MailServerController;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SocketManager implements Runnable {
    private final PersistentCounter counter;
    private final UserHandler userHandler;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final MailServerController controllerView;
    private final ExecutorService serverGui;

    public SocketManager(ObjectInputStream in, ObjectOutputStream out, MailServerController controller,
            UserHandler userHandler, PersistentCounter counter, ExecutorService serverGui) {
        objectInputStream = in;
        objectOutputStream = out;
        controllerView = controller;
        this.userHandler = userHandler;
        this.serverGui = serverGui;
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            Gson x = new Gson();
            // res is String because we're reading a JSON string
            String res = (String) objectInputStream.readObject();
            System.out.println(res);
            boolean exit = false;
            // Deserialize JSON string to Email object
            UserOperations userOperations = x.fromJson(res, UserOperations.class);
            ServerResponse response = new ServerResponse(true, null);
            if(userOperations.getOperation() == Operation.EXIT) { exit = true; }
            doOperation(userOperations, response);
            if(!exit) {
                objectOutputStream.writeObject(new Gson().toJson(response));
            }
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound " + e);
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception " + e);
        }
    }

    // Method to handle user operations then set the response accordingly
    public void doOperation(UserOperations userOperations, ServerResponse response)
            throws InterruptedException, IOException {
        String username;
        boolean result;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentData = new Date();
        String currentTime = timeFormat.format(currentData);
        switch (userOperations.getOperation()) {
            case LOGIN: {
                username = userOperations.getUsername();
                result = userHandler.checkUserExists(username);
                // take date
                if (result) {
                    serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.LOGIN));
                    response.setMessage("Correct Login");
                } else
                    response.setMessage("User not exist");
                response.setSuccess(result);
                break;
            }
            case REGISTER: {
                username = userOperations.getUsername();
                result = userHandler.checkUserExists(username);
                if(result) {
                    response.setMessage("User already exists");
                    response.setSuccess(false);
                    break;
                }
                result = userHandler.writeMailbox(userOperations.getMailBox());
                if (result)
                    response.setMessage("Welcome " + username);
                else
                    response.setMessage("Access denied");
                response.setSuccess(result);
                break;
            }
            case EXIT: {
                username = userOperations.getUsername();
                serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.EXIT));
                userHandler.resetRefreshTime(username);
                userHandler.resetIdReceive(username);
                userHandler.resetIdSent(username);
                response.setSuccess(true);
                response.setMessage("Bye");
                break;
            }
            case SEND: {
                username = userOperations.getUsername();
                Email emailToBeSent = userOperations.getToSend();
                List<String> usersToSend = emailToBeSent.getRecipients();
                boolean control;
                for (String user : usersToSend) {
                    control = userHandler.checkUserExists(user);
                    if (!control) {
                        response.setSuccess(false);
                        response.setMessage("Wrong User");
                    }
                }
                if (response.isSuccess()) {
                    for (String user : usersToSend) {
                        //System.out.println(user);
                        MailBox userToSend = userHandler.readUserMailbox(user);
                        //System.out.println("Aggiungo email a " + userToSend.getMailBoxOwner());
                        emailToBeSent.setID(counter.increment());

                        userToSend.addReceivedEmail(emailToBeSent);
                        userHandler.writeMailbox(userToSend);
                        //System.out.println("Aggiunta");
                    }
                    MailBox sender = userHandler.readUserMailbox(userOperations.getUsername());
                    //System.out.println(sender.getMailBoxOwner());
                    sender.addSentEmail(emailToBeSent);
                    userHandler.writeMailbox(sender);
                    currentData = new Date();
                    currentTime = timeFormat.format(currentData);
                    serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.SEND,
                            userOperations.getSendType(), emailToBeSent.getRecipients()));

                    response.setSuccess(true);
                    response.setMessage("Correct send email");
                } else
                    serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.ERROR,
                            userOperations.getSendType(), emailToBeSent.getRecipients()));
                break;
            }
            case UPDATE: {
                username = userOperations.getUsername();
                Date lastRefreshTime = userHandler.getLastRefreshTime(username);
                Date newRefreshTime = userOperations.getLastUpdate();
                Date firstTime = new Date(0);
                if(lastRefreshTime.equals(firstTime) && !newRefreshTime.equals(firstTime) && lastRefreshTime.equals(firstTime)){
                    userHandler.updateLastRefreshTime(username, newRefreshTime);
                    MailBox user = userHandler.readUserMailbox(username);
                    if(user.getsEmails().isEmpty()){
                        userHandler.updateLastIdSent(username, 0);
                    }
                    else {
                        userHandler.updateLastIdSent(username, user.getsEmails().getFirst().getID());
                    }
                    if(user.getrEmails().isEmpty()){
                        userHandler.updateLastIdReceive(username, 0);
                    }
                    else {
                        userHandler.updateLastIdReceive(username, user.getrEmails().getFirst().getID());
                    }
                }
                else{
                ArrayList<Email> newSEmails = userHandler.getNewSEmails(username);
                ArrayList<Email> newREmails = userHandler.getNewREmails(username);
                userHandler.updateLastRefreshTime(username, newRefreshTime);
                if ((newREmails != null && !newREmails.isEmpty()) || (newSEmails != null && !newSEmails.isEmpty())) {
                    response.sendMailbox(new MailBox(newREmails, newSEmails, username), objectOutputStream);
                    response.setSuccess(true);
                    if((newREmails != null && !newREmails.isEmpty()) && !firstTime.equals(newRefreshTime)) {
                        serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.RECEIVE));
                    }
                } else {
                    response.setMessage("No new emails since last refresh");
                    response.setSuccess(false);
                }}
                break;
            }
            case DELETE:
                username = userOperations.getUsername();
                result = userHandler.deleteEmailFromMailbox(username, userOperations.getToDelete(),
                        userOperations.getType());
                if (result) {
                    serverGui.submit(new ThreadGui(controllerView, username, currentTime, Operation.DELETE));
                    response.setMessage("Login corretto");
                } else
                    response.setMessage("User not exist");
                response.setSuccess(result);
                break;
            default:
                break;
        }
    }
}
