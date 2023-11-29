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
    private StringProperty mailBoxOwner = new SimpleStringProperty();

    private MailBox mailBox;
    public ClientModel() {
        this.mailBox = new MailBox();
        syncWithMailBox();
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    private void syncWithMailBox() {
        mailBoxOwner.set(mailBox.getMailBoxOwner());
        rEmails.setAll(FXCollections.observableArrayList(mailBox.getrEmails()));
        sEmails.setAll(FXCollections.observableArrayList(mailBox.getsEmails()));
    }

    // Metodi get e set che operano sulle property di JavaFX
    public StringProperty mailBoxOwnerProperty() {
        return mailBoxOwner;
    }

    public ListProperty<Email> rEmailsProperty() {
        return rEmails;
    }

    public  ListProperty<Email> sEmailsProperty() {
        return sEmails;
    }
    //TODO GESTIRE L'ID
    //TODO GESTIONE DI CONTROLLO CORRETTEZZA MAIL E TUTTI I CAMPI ALLE MAIL
    //TODO POPUP MAIL ERRATA LATO CLIENT, POPUP MANCATO INVIO MAIL COMUNICATO DAL SERVER, POPUP PER ELIMINA, POPUP NUOVO MESSAGGIO

    public boolean CorrectFormatEmail(List<String> recipients) {
        String emailRegex = "^([0-9]|[a-z])+([0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        boolean correct = true;
        for (int i = 0; i < recipients.size() && correct; i++) {
            Matcher matcher = pattern.matcher(recipients.get(i));

            correct = matcher.find();

        }
        return correct;
    }

    public void send(Email email){
        System.out.println(email);
    }
    public void remove(Email email){
        System.out.println(email);
    }


}
