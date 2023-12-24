package com.example.serverMail.model;

import java.nio.file.attribute.UserPrincipal;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.example.serverMail.controller.MailServerController;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketManager implements Runnable {

    private final Socket clientSocket;
    private final UserHandler userHandler;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final MailServerController controllerView;


    public SocketManager(Socket clientSocket, ObjectInputStream in, ObjectOutputStream out, MailServerController  controller) {
        this.clientSocket = clientSocket;
        objectInputStream = in;
        objectOutputStream = out;
        controllerView = controller;
        userHandler = new UserHandler();
    }

    @Override
    public void run() {
        try {
            Gson x = new Gson();
            String res = (String) objectInputStream.readObject();
            System.out.println(res);

            // Deserialize JSON string to Email object
            UserOperations o = x.fromJson(res, UserOperations.class);
            ServerResponse response = new ServerResponse(true,null);
            doOperation(o, controllerView, response);
            objectOutputStream.writeObject(new Gson().toJson(response));
        } catch (IOException e) {
            System.out.println("IOException " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound " + e);
        }catch (InterruptedException e)
        {
            System.out.println("Interrupted exception " +e);
        }
    }

    public void doOperation(UserOperations userOperations, MailServerController controllerView, ServerResponse response) throws InterruptedException, IOException {
        String username;
        boolean result;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentData = new Date();
        String currentTime = timeFormat.format(currentData);
        switch (userOperations.getOperation()) {
            case LOGIN: {
                username = userOperations.getUsername();
                result = userHandler.checkUserExists(userOperations.getUsername());
                //take date
                if (result) {
                    Thread t1 = new Thread(new ThreadGui(controllerView, username, currentTime, Operation.LOGIN));
                    t1.start();
                    t1.join();
                    response.setMessage("Login corretto");
                }
                else
                    response.setMessage("User not exist");
                response.setSuccess(result);
                break;
            }
            case REGISTER:
                    result = userHandler.writeMailbox(userOperations.getMailBox());
                    if(result)
                        response.setMessage("welcome " + userOperations.getUsername() );
                    else
                        response.setMessage("Access denied");
                    response.setSuccess(result);
                break;
            case EXIT: {
                username = userOperations.getUsername();
                    Thread t1 = new Thread(new ThreadGui(controllerView, username, currentTime, Operation.EXIT));
                    t1.start();
                    t1.join();
                    response.setSuccess(true);
                    response.setMessage("Bye");
                    break;
            }
            case SEND: {
                username = userOperations.getUsername();
                Email temp = userOperations.getToSend();
                System.out.println(temp.toString());
                List<String> userTotest = temp.getRecipients();
                boolean control = true;
                for (String user : userTotest) {
                    control = userHandler.checkUserExists(user);
                    if (!control) {
                        response.setSuccess(false);
                        response.setMessage("Wrong User");
                    }
                }
                if (control) {
                    for (String s : userTotest) {
                        System.out.println(s);
                        MailBox prov = userHandler.readUserMailbox(s);
                        System.out.println("Aggiungo email a " + prov.getMailBoxOwner());
                        PersistentCounter ID = new PersistentCounter();
                        temp.setID(ID.increment());
                        prov.addReceivedEmail(temp);
                        userHandler.writeMailbox(prov);
                        System.out.println("Aggiunta");
                    }
                    MailBox sender = userHandler.readUserMailbox(userOperations.getUsername());
                    System.out.println(sender.getMailBoxOwner());
                    sender.addSentEmail(temp);
                    userHandler.writeMailbox(sender);
                    currentData = new Date();
                    currentTime = timeFormat.format(currentData);
                    Thread send = new Thread(new ThreadGui(controllerView, username, currentTime, Operation.SEND, userOperations.getSendType(), temp.getRecipients()));
                    send.start();
                    send.join();

                    response.setSuccess(true);
                    response.setMessage("Correct send email");
                }
                break;
            }
            case UPDATE: {
                username = userOperations.getUsername();
                MailBox mailbox = userHandler.readUserMailbox(username);
                if (mailbox != null) {
                    response.sendMailbox(mailbox, objectOutputStream);
                    response.setSuccess(true);
                } else {
                    response.setMessage("Mailbox not found");
                    response.setSuccess(false);
                }
                break;
            }
            case DELETE:
                username = userOperations.getUsername();
                result = userHandler.deleteEmailFromMailbox(username, userOperations.getToDelete(), userOperations.getType());
                if (result) {
                    Thread t1 = new Thread(new ThreadGui(controllerView, username, currentTime, Operation.DELETE));
                    t1.start();
                    t1.join();
                    response.setMessage("Login corretto");
                }
                else
                    response.setMessage("User not exist");
                response.setSuccess(result);
                break;
        }
        }
    }
