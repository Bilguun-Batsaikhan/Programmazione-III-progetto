package com.example.serverMail.model;
import java.io.*;

public class PersistentCounter {
    private static final String FILE_PATH = "counter.txt";
    private int counter;

    public PersistentCounter() {
        this.counter = readCounterFromFile();
    }

    public synchronized int increment() {
        counter++;
        writeCounterToFile();
        return counter;
    }

    private void writeCounterToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(String.valueOf(counter));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized int readCounterFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line) : 0;
        } catch (FileNotFoundException e) {
            return 0; // if the file doesn't exist, return 0
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCounter() {
        return counter;
    }

}
