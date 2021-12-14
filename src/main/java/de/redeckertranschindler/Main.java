package de.redeckertranschindler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Server("E:\\Programmierprojekt\\germany.fmi", 80);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
