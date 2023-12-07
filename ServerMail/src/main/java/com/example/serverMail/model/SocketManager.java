package com.example.serverMail.model;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.example.serverMail.controller.MailServerController;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class SocketManager implements Runnable {

    private final Socket clientSocket;
    private UserHandler userHandler;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private MailServerController controllerView;


    public SocketManager(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out, MailServerController  controller) {
        this.clientSocket = clientSocket;
        objectInputStream = in;
        objectOutputStream = out;
        controllerView = controller;
        userHandler = new UserHandler();
    }

    @Override
    public void run() {
        try {
            Gson x = new Gson();
            String res = (String) objectInputStream.readObject();
            System.out.println(res);

            // Deserialize JSON string to Email object
            UserOperations o = x.fromJson(res, UserOperations.class);

            ServerResponse response = new ServerResponse(doOperation(o, controllerView));
            objectOutputStream.writeObject(new Gson().toJson(response));
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound " + e);
        }catch (InterruptedException e)
        {
            System.out.println("Interrupted exception " +e);
        }
    }

    public String doOperation(UserOperations userOperations, MailServerController controllerView) throws InterruptedException{
        String username;
        boolean result;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        switch (userOperations.getNumOperation()) {
            case 1: {
                username = userOperations.getUsername();
                result = userHandler.verifyUser(userOperations.getUsername());
                //take date
                if(result){
                    Date currentData = new Date();
                    String currentTime = timeFormat.format(currentData);
                    Thread t1 = new Thread(new ThreadGui(controllerView, username, currentTime,0));
                    t1.start();
                    t1.join();
                }
                return result ? "welcome " + username : "Access denied";
            }
            case 2:
                try {
                    result = userHandler.addUser(userOperations.getMailBox());
                    return result ? "welcome " + userOperations.getUsername() : "Access denied";
                } catch (IOException e) {
                    System.out.println("Failed to add user " + e);
                }
            break;
            case 3:
                username = userOperations.getUsername();
                result = userHandler.verifyUser(userOperations.getUsername());
                if(result)
                {
                    Date currentData = new Date();
                    String currentTime = timeFormat.format(currentData);
                    Thread t1 = new Thread(new ThreadGui(controllerView, username, currentTime,1));
                    t1.start();
                    t1.join();
                }
                return result ? "Bye " + username : "Error, user not joined";
        }
        return "There is no such operation";
    }
}