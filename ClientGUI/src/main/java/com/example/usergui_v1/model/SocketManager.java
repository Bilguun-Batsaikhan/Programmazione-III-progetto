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
import java.util.concurrent.atomic.AtomicReference;


public class SocketManager {
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

    public void setUsername(String username){
        this.username=username;
    }

    public boolean setEmailToSend(Email toSend, SendType sendType){
            List<String> receiver = toSend.getRecipients();
            for (String temp : receiver) {
                if (temp.equals(username))
                    return false;
            }
            this.toSend = toSend;
            this.sendType = sendType;
            return startSocket(Operation.SEND);
    }

    public  boolean setEmailToDelete(Email toDelete){
        this.toDelete= toDelete;
        return startSocket(Operation.DELETE);
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

            System.out.println("reached here");

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

    public boolean startSocket(Operation LogReg){
        switch (LogReg) {
            case LOGIN:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName,8080);
                    UserOperations askAuthentication = new UserOperations(Operation.LOGIN, username);
                    System.out.println(username);
                    askAuthentication.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = askAuthentication.receiveServerResponse(socketManager.getObjectInputStream());
                    if(!response.isSuccess()){
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
                    UserOperations register = new UserOperations(Operation.REGISTER, new MailBox(new ArrayList<>(), new ArrayList<>(), username), username);
                    register.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = register.receiveServerResponse(socketManager.getObjectInputStream());
                    if(!response.isSuccess()) {
                        System.out.println(response.getMessage());
                        responseRegister = response.getMessage();
                        return false;
                    } else
                        return true;

                } catch (IOException e) {
                    System.out.println("Registration failed " + e);
                }
                break;
            case EXIT:
                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    SocketManager socketManager = new SocketManager(hostName, 8080);
                    UserOperations left = new UserOperations(Operation.EXIT, username);
                    left.sendRequest(socketManager.getObjectOutputStream()); //request log out
                    socketManager.closeConnection();
                } catch (UnknownHostException e) {
                    System.out.println("Log out failed " + e);
                } catch (IOException | NullPointerException e) {
                    throw new RuntimeException(e);
                }
                break;
             case SEND: {
                 try {
                     String hostName = InetAddress.getLocalHost().getHostName();
                     SocketManager socketManager = new SocketManager(hostName, 8080);
                     UserOperations sendEmail = new UserOperations(Operation.SEND,this.sendType, username, this.toSend);
                     sendEmail.sendRequest(socketManager.getObjectOutputStream());
                     ServerResponse response = sendEmail.receiveServerResponse(socketManager.objectInputStream);
                     if (!response.isSuccess()) {
                         System.out.println(response.getMessage());
                         return false;
                     }
                     socketManager.closeConnection();
                 } catch (IOException e) {
                     System.out.println("Error while sending email" + e);
                 }
                 catch (NullPointerException e){
                     throw new NullPointerException();
                 }
                 break;
             }
            case DELETE: {
                try{
                String hostName = InetAddress.getLocalHost().getHostName();
                SocketManager socketManager = new SocketManager(hostName, 8080);
                UserOperations deleteEmail = new UserOperations(Operation.DELETE,null, username, null, null, this.toDelete, false, null, type, null);
                deleteEmail.sendRequest(socketManager.getObjectOutputStream());
                ServerResponse response = deleteEmail.receiveServerResponse(socketManager.objectInputStream);
                if (!response.isSuccess()) {
                    System.out.println(response.getMessage());
                    return false;
                }
                socketManager.closeConnection();
            } catch (IOException | NullPointerException e) {
                    System.out.println("There is a problem while deleting email" + e);
            }
            }
            default:
                break;
        }
        return true;
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
            SocketManager socketManager = new SocketManager(hostName, 8080);
            UserOperations mailboxRequest;
            if(first_load){
                mailboxRequest = new UserOperations(Operation.UPDATE, this.username, new Date (0));
            }
            else{
                mailboxRequest = new UserOperations(Operation.UPDATE, this.username , new Date());
            }
            mailboxRequest.sendRequest(socketManager.getObjectOutputStream());

            MailBox updatedMailbox = mailboxRequest.receiveUpdatedMailbox(socketManager.getObjectInputStream());
            socketManager.closeConnection();
            return updatedMailbox;

        } catch (IOException | NullPointerException e) {
            System.out.println("Error in getting updated mailbox: " + e.getMessage());
        }
        return null;
    }


    //email recived or send -> see server socket, true = recive, false = send
    public void setType(Boolean type){this.type = type;}

    public void Refresh(ControllerList temp, String username) {
        try {
            this.username = username;
            new Thread(() -> {
                int count = 0;
                while (true) {
                    try {
                        MailBox updated;
                        if(count == 0){
                            updated = getUpdatedMailbox(true);

                        }
                        else{
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

}
