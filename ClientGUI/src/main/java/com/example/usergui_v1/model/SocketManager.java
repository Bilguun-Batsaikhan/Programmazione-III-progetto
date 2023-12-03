package com.example.usergui_v1.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketManager {
    private Socket socket;
    private String username;
    public SocketManager(String serverAddress, String username, int port) {
        try {
            this.username = username;
            this.socket = new Socket(serverAddress, port);
        } catch (IOException e) {
            System.out.println("There is a problem");
        }
    }

    public void sendRequest() {
        try {
            OutputStream outputStream = socket.getOutputStream();

            // Convert the username String to bytes
            byte[] usernameBytes = username.getBytes("UTF-8");

            // Send the length of the username as a 4-byte integer
            outputStream.write(usernameBytes.length >> 24);
            outputStream.write(usernameBytes.length >> 16);
            outputStream.write(usernameBytes.length >> 8);
            outputStream.write(usernameBytes.length);

            outputStream.write(usernameBytes);

            outputStream.flush();
        } catch (IOException e) {
            System.out.println("There is a problem");
        }
    }

    public String receiveResponse() {
        try {
            InputStream inputStream = socket.getInputStream();

            int length = 0;

            // Read the length
            length |= readNextByte(inputStream) << 24;
            length |= readNextByte(inputStream) << 16;
            length |= readNextByte(inputStream) << 8;
            length |= readNextByte(inputStream);

            // Read the response data
            byte[] serverResponseBytes = new byte[length];
            int bytesRead = 0;
            while (bytesRead < length) {
                int result = inputStream.read(serverResponseBytes, bytesRead, length - bytesRead);
                if (result == -1) {
                    // Handle end of stream or other errors
                    throw new IOException("Unexpected end of stream or other error");
                }
                bytesRead += result;
            }

            String receivedResponse = new String(serverResponseBytes, StandardCharsets.UTF_8);
            return receivedResponse;
        } catch (IOException e) {
            System.out.println("There is a problem");
        }
        return null;
    }

    private int readNextByte(InputStream inputStream) throws IOException {
        int result = inputStream.read();
        if (result == -1) {
            throw new IOException("Unexpected end of stream or other error");
        }
        return result;
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
