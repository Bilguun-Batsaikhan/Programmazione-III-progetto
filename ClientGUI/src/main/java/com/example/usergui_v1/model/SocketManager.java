package com.example.usergui_v1.model;
import com.example.usergui_v1.controller.ControllerPopUp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class SocketManager {
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private String username;

    private Email toSend;
    private SendType sendType;

    private Email toDelete;
    private boolean type;

    public SocketManager() {
    }

    public void setUsername(String username){
        this.username=username;
    }

    public boolean setEmailToSend(Email toSend, SendType sendType){
        List<String> receiver = toSend.getRecipients();
        for(String temp: receiver)
        {
            if(temp.equals(username))
                return false;
        }
        this.toSend=toSend;
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
                    UserOperations register = new UserOperations(Operation.REGISTER, new MailBox(new ArrayList<>(), new ArrayList<>(), username));
                    register.sendRequest(socketManager.getObjectOutputStream());
                    ServerResponse response = register.receiveServerResponse(socketManager.getObjectInputStream());
                    if(!response.isSuccess()) {
                        System.out.println(response.getMessage());
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
                } catch (IOException e) {
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
                     throw new RuntimeException(e);
                 }
             }
             break;
            case DELETE: {
                try{
                String hostName = InetAddress.getLocalHost().getHostName();
                SocketManager socketManager = new SocketManager(hostName, 8080);
                UserOperations deleteEmail = new UserOperations(Operation.DELETE,null, username, null, null, this.toDelete, false, null, type);
                deleteEmail.sendRequest(socketManager.getObjectOutputStream());
                ServerResponse response = deleteEmail.receiveServerResponse(socketManager.objectInputStream);
                if (!response.isSuccess()) {
                    System.out.println(response.getMessage());
                    return false;
                }
                socketManager.closeConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public MailBox getMailbox() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            SocketManager socketManager = new SocketManager(hostName, 8080);
            UserOperations mailboxRequest = new UserOperations(Operation.UPDATE, this.username);
            mailboxRequest.sendRequest(socketManager.getObjectOutputStream());
            MailBox mailbox = mailboxRequest.receiveMailbox(socketManager.getObjectInputStream());
            socketManager.closeConnection();
            return mailbox;
        } catch (IOException e) {
            System.out.println("Error in getting mailbox: " + e.getMessage());
        }
        return null;
    }
    //email recived or send -> see server socket, true = recive, false = send
    public void setType(Boolean type){this.type = type;}
}
