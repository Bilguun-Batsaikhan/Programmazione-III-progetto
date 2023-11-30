package com.example.serverMail.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserHandler {

    String fileName = "usernames.bin";
    public Boolean addUser(String userName) {
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] usernameBytes = userName.getBytes(StandardCharsets.UTF_8);
            // Write the length of the username as a 4-byte integer
            outputStream.write(usernameBytes.length >> 24);
            outputStream.write(usernameBytes.length >> 16);
            outputStream.write(usernameBytes.length >> 8);
            outputStream.write(usernameBytes.length);

            // Write the actual username bytes
            outputStream.write(usernameBytes);
            return true;
        } catch (IOException e) {
            System.out.println(e + " File not found");
        }
        return false;
    }

    public List<String> readUsers() {
        List<String> usernames = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(fileName)) {
            while (true) {

                int length = 0;
                length |= inputStream.read() << 24;
                length |= inputStream.read() << 16;
                length |= inputStream.read() << 8;
                length |= inputStream.read();


                if (length == -1) {
                    break;
                }


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
