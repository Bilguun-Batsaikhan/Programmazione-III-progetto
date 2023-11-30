package com.example.serverMail.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private final ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                // Handle the connection using a SocketManager
                executorService.submit(new SocketManager(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
