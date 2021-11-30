package de.redeckertranschindler;

import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) {
        try {

            // new Graph("E:\\Programmierprojekt\\germany.fmi"); // Danny
            // new Graph("E:\\Programmierprojekt\\germany.fmi"); // Mickey
            Graph g = new Graph("E:\\Programmierprojekt\\germany.fmi"); // Simon

            System.out.println(g.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
