package com.example.usergui_v1.model;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ClientModel {
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