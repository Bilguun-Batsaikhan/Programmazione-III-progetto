package com.example.usergui_v1.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ClientModel {
    private ListProperty<Email> rEmails = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Email> sEmails = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty mailBoxOwner = new SimpleStringProperty();

    private MailBox mailBox;
    public ClientModel() {
        this.mailBox = new MailBox();
        syncWithMailBox();
    }

    //TODO GESTIRE L'ID CON CONTATORE ATOMICO
//TODO DA FARE DOPO CHE I SOCKET FUNZIONANO POPUP MANCATO INVIO MAIL COMUNICATO DAL SERVER, POPUP PER ELIMINA, POPUP NUOVO MESSAGGIO, CRASH SERVER
//TODO RIDIMENSIONE FINESTRE NON SO SE VOGLIO FARLO

    public MailBox getMailBox() {
        return mailBox;
    }

    //L'ottenimento della casella di posta dovrebbe essere collegato a questo metodo. In questo modo la casella di posta ottenuta dal server
    //viene passata con le properties al client
    private void syncWithMailBox() {
        mailBoxOwner.set(mailBox.getMailBoxOwner());
        rEmails.setAll(FXCollections.observableArrayList(mailBox.getrEmails()));
        sEmails.setAll(FXCollections.observableArrayList(mailBox.getsEmails()));
    }

    public StringProperty mailBoxOwnerProperty() {
        return mailBoxOwner;
    }

    public ListProperty<Email> rEmailsProperty() {
        return rEmails;
    }

    public  ListProperty<Email> sEmailsProperty() {
        return sEmails;
    }


    public boolean CorrectFormatEmail(List<String> recipients) {
        String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        boolean correct = true;
        for (int i = 0; i < recipients.size() && correct; i++) {
            Matcher matcher = pattern.matcher(recipients.get(i));

            correct = matcher.find();

        }
        return correct;
    }

    //Questa è la funzione che dovrebbe occuparsi di mandare il messaggio al server
    public void send(Email email){
        System.out.println(email);
    }
    //Questa è la funzione che dovrebbe occuparsi di chiedere al server di eliminare il messaggio
    public void remove(Email email){
        System.out.println(email);
    }


}
