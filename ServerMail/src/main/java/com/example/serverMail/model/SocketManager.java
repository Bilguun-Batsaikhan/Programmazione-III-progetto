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
        userHandler = controllerView.getUserHandler();
    }

    @Override
    public void run() {
        try {
            Gson x = new Gson();
            String res = (String) objectInputStream.readObject();
            System.out.println(res);

            // Deserialize JSON string to Email object
            UserOperations o = x.fromJson(res, UserOperations.class);
            System.out.println(o.getUsername());

            ServerResponse response = new ServerResponse(doOperation(o, controllerView));
            objectOutputStream.writeObject(new Gson().toJson(response));
            // objectOutputStream.writeObject(new Gson().toJson(answer));
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound " + e);
        }
    }

    public String doOperation(UserOperations userOperations, MailServerController controllerView) {
        switch (userOperations.getNumOperation())
        {
            case 1:
            {
                String username = userOperations.getUsername();
                //take date
                Date dataCorrente = new Date();
                SimpleDateFormat formatoOrario = new SimpleDateFormat("HH:mm:ss");
                String orarioCorrente = formatoOrario.format(dataCorrente);
                //add a listView
                controllerView.addLogMessageLogin(username + " si è connesso alle " + orarioCorrente);
                boolean result = verifyUser(username);
                String test = result ? "welcome " + username : "Access denied";
                return "welcome " + username;
            }
        }
        return "There is no such operation";
    }

    public boolean verifyUser(String userName) {
        userHandler = new UserHandler();
        List<String> users = userHandler.readUsers();
        for (String user : users) {
            if (user.equals(userName)) {
                return true;
            }
        }
        return false;
    }
}