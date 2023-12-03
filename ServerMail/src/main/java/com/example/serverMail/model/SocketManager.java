package com.example.serverMail.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SocketManager implements Runnable {

    private final Socket clientSocket;
    private UserHandler userHandler;
    public SocketManager(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            // Step 1: Read the length of the username
            int length = 0;

            length |= inputStream.read() << 24;
            length |= inputStream.read() << 16;
            length |= inputStream.read() << 8;
            length |= inputStream.read();

            // Step 2: Read the username
            byte[] usernameBytes = new byte[length];
            inputStream.read(usernameBytes);
            String username = new String(usernameBytes, StandardCharsets.UTF_8);

            String response = verifyUser(username) ? "Welcome " + username : "Access denied";

            // Send the response back to the client
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            outputStream.write(responseBytes.length >> 24);
            outputStream.write(responseBytes.length >> 16);
            outputStream.write(responseBytes.length >> 8);
            outputStream.write(responseBytes.length);

            outputStream.write(responseBytes);

            // Flush the output stream to ensure all data is sent
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean verifyUser(String userName) {
        userHandler = new UserHandler();
        List<String> users = userHandler.readUsers();
        for(String user: users) {
            if(user.equals(userName)) {
                return true;
            }
        }
        return false;
    }
}