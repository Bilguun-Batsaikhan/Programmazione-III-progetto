package com.example.usergui_v1.model;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ClientModel {
    private ListProperty<Email> rEmails = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Email> sEmails = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty mailBoxOwner = new SimpleStringProperty();

    private MailBox mailBox;

    public ClientModel() {
    }

    public StringProperty mailBoxOwnerProperty() {
        return mailBoxOwner;
    }

    public ListProperty<Email> rEmailsProperty() {
        return rEmails;
    }

    public ListProperty<Email> sEmailsProperty() {
        return sEmails;
    }


    public boolean CorrectFormatEmail(List<String> recipients) {
        if(recipients == null){
            return false;
        }
        String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        boolean correct = true;
        for (int i = 0; i < recipients.size() && correct; i++) {
            Matcher matcher = pattern.matcher(recipients.get(i));

            correct = matcher.find();

        }
        return correct;
    }

    public void addSentEmail(Email email) {
        sEmails.add(email);
    }

    public List<Email> getSendEmail() {
        return sEmails;
    }
}