package de.redeckertranschindler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {
    public Graph() {

        long time = System.nanoTime();

        int n = 0;
        int m = 0;

        double[][] coordinates = new double[0][0];
        int[] offset = new int[0];
        int[][] adj = new int[0][0];

        try {
            File myObj = new File("P:\\InputFileTest\\germany.fmi");
            Scanner myReader = new Scanner(myObj);
            for (int i = 0; i < 5; i++) {
                myReader.nextLine();
            }

            n = Integer.parseInt(myReader.nextLine());
            m = Integer.parseInt(myReader.nextLine());

            coordinates = new double[2][n];
            offset = new int[n];
            adj = new int[3][m];

            System.out.println("nodes " + n);
            System.out.println("edges " + m);

            for (int i = 0; i < n; i++) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");

                int id = Integer.parseInt(parts[0]);
                coordinates[0][id] = Double.parseDouble(parts[2]);
                coordinates[1][id] = Double.parseDouble(parts[3]);

            }

            int src = 0;
            int counter = 0;

            for (int i = 0; i < m; i++) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");
                int currentSrc = Integer.parseInt(parts[0]);
                if (currentSrc != src) {
                    offset[src] = counter;

                    src = currentSrc;
                    counter = 0;
                }

                counter++;

                adj[0][i] = Integer.parseInt(parts[0]);
                adj[1][i] = Integer.parseInt(parts[1]);
                adj[2][i] = Integer.parseInt(parts[2]);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println(((System.nanoTime() - time) / 1000000000) + " Sek.");

    }

}
