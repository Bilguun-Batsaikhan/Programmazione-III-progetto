package com.example.usergui_v1.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ClientModel {
    private final StringProperty mailBoxOwner = new SimpleStringProperty();

    public ClientModel() {
    }

    public StringProperty mailBoxOwnerProperty() {
        return mailBoxOwner;
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
}