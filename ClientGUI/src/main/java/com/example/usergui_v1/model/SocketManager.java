package com.example.usergui_v1.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketManager {
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;

    public SocketManager(String serverAddress, int port) {
        try {
            this.socket = new Socket(serverAddress, port);
            System.out.println("User socket initialized");

            // Create ObjectInputStream first
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("ObjectInputStream created");

            // Then create ObjectOutputStream
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ObjectOutputStream created");

            System.out.println("reached here");

            String signal = null;
            try {
                signal = (String) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Received signal from server: " + signal);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Connection closed successfully.");
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }


//    public void closeConnection() {
//        try {
//            if (socket != null && !socket.isClosed()) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public Socket getSocket() {
        return socket;
    }
}
