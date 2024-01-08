package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private  UserHandler userHandler;
    private final int port;
    private final ExecutorService executorService;
    private final ExecutorService serverGui;
    private final MailServerController controllerView;

    public Server(int port, MailServerController controller, UserHandler userHandler) {
        this.port = port;
        this.userHandler= userHandler;
        this.executorService = Executors.newCachedThreadPool();
        this.serverGui = Executors.newCachedThreadPool();
        this.controllerView = controller;

    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // Server initiates communication
                out.writeObject("Hello from server!");
                // Handle the connection using a SocketManager

                executorService.submit(new SocketManager(in, out, controllerView, userHandler, new PersistentCounter(), serverGui));
            }
        } catch (IOException e) {
            System.out.println("There is an exception in server socket " +e);
        }
    }
}
