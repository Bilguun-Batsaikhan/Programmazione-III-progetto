package com.example.serverMail.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UserHandler {

    String fileName = "usernames.bin";
    private final String emailRegex = "^([0-9]|[a-z])+((\\.)|[0-9]|[a-z])*+@[a-z]+(\\.[a-z]+)*\\.(it|com)$";
    private final Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

    public boolean addUser(String userName) throws IOException {
        if (!isEmailValid(userName)) {
            return false;
        }
        try (OutputStream outputStream = new FileOutputStream(fileName, true)) {
            byte[] usernameBytes = userName.getBytes(StandardCharsets.UTF_8);

            // Write the length of the username as a 4-byte integer
            outputStream.write((usernameBytes.length >> 24) & 0xFF);
            outputStream.write((usernameBytes.length >> 16) & 0xFF);
            outputStream.write((usernameBytes.length >> 8) & 0xFF);
            outputStream.write((usernameBytes.length) & 0xFF);

            // Write the actual username bytes
            outputStream.write(usernameBytes);

            return true;
        }
    }

    private boolean isEmailValid(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public List<String> readUsers() {
        List<String> usernames = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(fileName)) {
            while (true) {
                int length = 0;

                // Read the length of the username as a 4-byte integer
                int byte1 = inputStream.read();
                int byte2 = inputStream.read();
                int byte3 = inputStream.read();
                int byte4 = inputStream.read();

                if (byte1 == -1 || byte2 == -1 || byte3 == -1 || byte4 == -1) {
                    break;
                }

                length |= (byte1 << 24) & 0xFF000000;
                length |= (byte2 << 16) & 0x00FF0000;
                length |= (byte3 << 8) & 0x0000FF00;
                length |= (byte4) & 0x000000FF;

                byte[] usernameBytes = new byte[length];
                inputStream.read(usernameBytes);

                // Convert the bytes to a String using UTF-8 encoding
                String username = new String(usernameBytes, StandardCharsets.UTF_8);

                usernames.add(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }
}
