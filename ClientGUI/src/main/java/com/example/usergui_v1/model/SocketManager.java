package com.example.usergui_v1.model;

import com.example.usergui_v1.controller.ControllerLogin;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

public class SocketManager {
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private String username;

    private Email toSend;

    private Email toDelete;

    public SocketManager() {
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setEmailToSend(Email toSend){
        this.toSend=toSend;
        System.out.println(toSend);
    }

    public  void setEmailToDelete(Email toDelete){
        this.toDelete= toDelete;
        System.out.println(toDelete);
    }


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
    public boolean startSocket(Operation LogReg) {
        switch (LogReg) {
            case LOGIN:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations askAuthentication = new UserOperations(Operation.LOGIN, username);
                    askAuthentication.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = askAuthentication.receiveServerResponse(socketManager.getObjectInputStream());
                    if(response.getMessage().equals("Access denied")) {
                        System.out.println(response.getMessage());
                        return false;
                    }
                    socketManager.closeConnection();
                } catch (UnknownHostException e) {
                    System.out.println("Login failed " + e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case REGISTER:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations register = new UserOperations(Operation.REGISTER, new MailBox(new ArrayList<Email>(), new ArrayList<Email>(), username));
                    register.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = register.receiveServerResponse(socketManager.getObjectInputStream());
                    if(response.getMessage().equals("Access denied")) {
                        System.out.println(response.getMessage());
                        return false;
                    } else {
                        return true;
                    }
                } catch (UnknownHostException e) {
                    System.out.println("Registration failed " + e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case EXIT:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName, 8080);
                    UserOperations left = new UserOperations(Operation.EXIT, username);
                    left.sendRequest(socketManager.getObjectOutputStream()); //request log out
                    ServerResponse response = left.receiveServerResponse(socketManager.getObjectInputStream());
                    if (response.getMessage().equals("Error, user not joined")) {
                        System.out.println(response.getMessage());
                        return false;
                    }
                    socketManager.closeConnection();
                } catch (UnknownHostException e) {
                    System.out.println("Log out failed " + e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//            case SEND:
//                //////////////////
//                UserOperations userOperations = new UserOperations(Operation.SEND, username.getText(), toSend, null, null, false, null);
//                SocketManager socketManager = new SocketManager()
//                userOperations.sendRequest();
//                //////////////////
//            case DELETE:

//            break;
        }
        return true;
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
