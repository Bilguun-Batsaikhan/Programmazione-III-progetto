package com.example.serverMail.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketManager implements Runnable {

    private final Socket clientSocket;

    public SocketManager(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            // Step 1: Read the length of the username
            int length = 0;
            /* Little note on Bitwise or Operation
            * a     = 0000 0101
              b     = 0000 0011
              a | b = 0000 0111
            */
            length |= inputStream.read() << 24;
            length |= inputStream.read() << 16;
            length |= inputStream.read() << 8;
            length |= inputStream.read();

            // Step 2: Read the username
            byte[] usernameBytes = new byte[length];
            inputStream.read(usernameBytes);
            String username = new String(usernameBytes, StandardCharsets.UTF_8);

            // Process the username and prepare the response (for now, just send back a simple message)
            String response = "Hello, " + username + "!";

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
}