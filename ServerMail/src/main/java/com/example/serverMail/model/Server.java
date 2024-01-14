package com.example.serverMail.model;

import com.example.serverMail.controller.MailServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class Server {
    private final UserHandler userHandler; // handle user related actions
    private final int port; // port to listen on
    private final ExecutorService executorService; // thread pool for handling clients
    private final ExecutorService serverGui; // thread pool for updating GUI
    private final MailServerController controllerView;

    private final PersistentCounter persistentCounter = new PersistentCounter();

    public Server(int port, MailServerController controller, UserHandler userHandler) {
        this.port = port;
        this.userHandler = userHandler;
        this.executorService = Executors.newCachedThreadPool();
        this.serverGui = Executors.newCachedThreadPool();
        this.controllerView = controller;

    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                // Wait for a connection from a client, Note: accept method is blocking
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // Server initiates communication
                out.writeObject("Hello from server!");
                // Handle the connection using a SocketManager

                try {
                    executorService.submit(
                            new SocketManager(in, out, controllerView, userHandler, persistentCounter, serverGui));
                } catch (RejectedExecutionException e) {
                    System.err.println("Task could not be submitted for execution: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("There is an exception in server socket " + e);
        }
    }
}
