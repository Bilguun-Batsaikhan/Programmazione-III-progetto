package com.example.serverMail.model;

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

    public SocketManager(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out) {
        this.clientSocket = clientSocket;
        objectInputStream = in;
        objectOutputStream = out;
    }

    @Override
    public void run() {
        try {
            Gson x = new Gson();
            String res = (String) objectInputStream.readObject();
            System.out.println(res);

            // Deserialize JSON string to Email object
            UserOperations o = x.fromJson(res, UserOperations.class);

            ServerResponse response = new ServerResponse(doOperation(o));
            objectOutputStream.writeObject(new Gson().toJson(response));
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound " + e);
        }
    }

    public String doOperation(UserOperations userOperations) {
        switch (userOperations.getNumOperation()) {
            case 1:
                boolean result = verifyUser(userOperations.getUsername());
                System.out.println(userOperations.getUsername());
                return result ? "welcome " + userOperations.getUsername() : "Access denied";
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