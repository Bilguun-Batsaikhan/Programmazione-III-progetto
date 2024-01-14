package com.example.usergui_v1.model;

import com.example.usergui_v1.controller.ControllerList;
import com.example.usergui_v1.controller.ControllerPopUp;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SocketManager implements AutoCloseable {
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private String username;

    private Email toSend;
    private SendType sendType;

    private Email toDelete;
    private boolean type;
    private String responseRegister;

    public SocketManager() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean setEmailToSend(Email toSend, SendType sendType) throws IOException {
            List<String> receiver = toSend.getRecipients();
            for (String temp : receiver) {
                if (temp.equals(username))
                    return false;
            }
            this.toSend = toSend;
            this.sendType = sendType;
            return startSocket(Operation.SEND);
    }

    public boolean setEmailToDelete(Email toDelete) {
        try{this.toDelete = toDelete;
        return startSocket(Operation.DELETE);}
        catch (IOException e){
            return false;
        }
    }

    public SocketManager(String serverAddress, int port) throws IOException {
        try {
            this.socket = new Socket(serverAddress, port);
            System.out.println("User socket initialized");

            // Create ObjectInputStream first
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("ObjectInputStream created");

            // Then create ObjectOutputStream
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("ObjectOutputStream created");

            String signal;
            try {
                signal = (String) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Received signal from server: " + signal);
        } catch (IOException e) {
            System.out.println("IO exception: " + e.getMessage());
            ControllerPopUp popUp = new ControllerPopUp();
            popUp.startPopUp("ServerConnection", false);
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

    public String getResponseMessageRegister() {
        return responseRegister;
    }

    public boolean startSocket(Operation operationType) throws IOException {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Failed to get host name: " + e);
            return false;
        }

        try (SocketManager socketManager = new SocketManager(hostName, 8080)) {
            UserOperations userOperations;
            switch (operationType) {
                case LOGIN:
                    userOperations = new UserOperations(Operation.LOGIN, username);
                    break;
                case REGISTER:
                    userOperations = new UserOperations(Operation.REGISTER,
                            new MailBox(new ArrayList<>(), new ArrayList<>(), username), username);
                    break;
                case EXIT:
                    userOperations = new UserOperations(Operation.EXIT, username);
                    //userOperations.sendRequest(objectOutputStream); //request log out
                    break;
                case SEND:
                    userOperations = new UserOperations(Operation.SEND, this.sendType, username, this.toSend);
                    break;
                case DELETE:
                    userOperations = new UserOperations(Operation.DELETE, null, username, null, null, this.toDelete,
                            false, null, type, null);
                    break;
                default:
                    return true;
            }

            userOperations.sendRequest(socketManager.getObjectOutputStream());
            if (operationType == Operation.EXIT) {
                return true;
            }
            ServerResponse response = userOperations.receiveServerResponse(socketManager.getObjectInputStream());

            if (!response.isSuccess()) {
                System.out.println(response.getMessage());
                if (operationType == Operation.REGISTER) {
                    responseRegister = response.getMessage();
                }
                return false;
            }
            else{
                return true;
            }
        } catch (Exception e) {
            System.out.println("Operation failed: " + e);
            if(operationType == Operation.SEND){
                throw new IOException();
            }}
        return false;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public MailBox getUpdatedMailbox(boolean first_load) {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            try (SocketManager socketManager = new SocketManager(hostName, 8080)) {
                UserOperations mailboxRequest;
                if (first_load) {
                    mailboxRequest = new UserOperations(Operation.UPDATE, this.username, new Date(0));
                } else {
                    mailboxRequest = new UserOperations(Operation.UPDATE, this.username, new Date());
                }
                mailboxRequest.sendRequest(socketManager.getObjectOutputStream());

                MailBox updatedMailbox = mailboxRequest.receiveUpdatedMailbox(socketManager.getObjectInputStream());
                socketManager.closeConnection();
                return updatedMailbox;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Error in getting updated mailbox: " + e.getMessage());
            }

        } catch (IOException | NullPointerException e) {
            System.out.println("Error in getting updated mailbox: " + e.getMessage());
        }
        return null;
    }

    // email recived or send -> see server socket, true = recive, false = send
    public void setType(Boolean type) {
        this.type = type;
    }

    public void Refresh(ControllerList temp, String username) {
        try {
            this.username = username;
            new Thread(() -> {
                int count = 0;
                while (true) {
                    try {
                        MailBox updated;
                        if (count == 0) {
                            updated = getUpdatedMailbox(true);
                        } else {
                            updated = getUpdatedMailbox(false);
                        }
                        count++;
                        if (updated != null) {
                            System.out.println(updated);
                            Platform.runLater(() -> temp.setMailBox(updated));
                        }
                        Thread.sleep(6000);
                    } catch (InterruptedException | NullPointerException e) {
                        System.out.println("Error in update thread " + e);
                    }
                }
            }).start();
        } catch (NullPointerException e) {
            System.out.println("There was a problem while refreshing" + e);
        }
    }

    @Override
    public void close() throws Exception {

        if (socket != null) {
            socket.close();
        }
        if (objectInputStream != null) {
            objectInputStream.close();
        }
        if (objectOutputStream != null) {
            objectOutputStream.close();
        }
    }

}
