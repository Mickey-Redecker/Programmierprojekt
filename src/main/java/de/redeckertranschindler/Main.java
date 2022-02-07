package de.redeckertranschindler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            final String graphPath = args[1];
            final int port = Integer.parseInt(args[3]);
            new Server(graphPath, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
