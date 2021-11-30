package de.redeckertranschindler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {

    /** Coordinatelist index */
    public static final int X = 0;
    /** Coordinatelist index */
    public static final int Y = 1;

    /** Adjacencylist index */
    public static final int SRCNODE = 0;
    /** Adjacencylist index */
    public static final int TARGETNODE = 1;
    /** Adjacencylist index */
    public static final int WEIGHT = 2;

    private final String graphFilePath;

    /**
     * Number of Nodes
     */
    private final int n;
    /**
     * Number of Edges
     */
    private final int m;

    /**
     * Coordinates of all the Nodes
     * 
     * First dimension: Coordinateaxis
     * Second dimension: Node-Id
     */
    private final double[][] coordinates;

    /**
     * An offset for each Node to know where to iterate through the adjacencylist
     */
    private final int[] offset;

    /**
     * Adjacencylist of all Edges
     * 
     * First dimension: information
     * Second dimension: Node-Id
     */
    private final int[][] adjacencyList;

    /**
     * Generates a Graph ready to use!
     * 
     * @param graphFilePath path to the specificly formated .fmi file
     * @throws FileNotFoundException
     */
    public Graph(final String graphFilePath) throws FileNotFoundException {

        this.graphFilePath = graphFilePath;
        File graphFile = new File(graphFilePath);
        Scanner graphFileReader;
        try {
            graphFileReader = new Scanner(graphFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Die angegebene Graphdatei konnte nicht geöffnet werden!");
        }

        // We don't care about the first 5 lines
        for (int i = 0; i < 5; i++) {
            graphFileReader.nextLine();
        }

        n = Integer.parseInt(graphFileReader.nextLine());
        m = Integer.parseInt(graphFileReader.nextLine());

        coordinates = new double[2][n];
        offset = new int[n];
        adjacencyList = new int[3][m];

        for (int i = 0; i < n; i++) {
            String data = graphFileReader.nextLine();
            String[] parts = data.split(" ");

            int id = Integer.parseInt(parts[0]);
            coordinates[X][id] = Double.parseDouble(parts[2]);
            coordinates[Y][id] = Double.parseDouble(parts[3]);

        }

        int src = 0;
        int counter = 0;

        for (int i = 0; i < m; i++) {
            String data = graphFileReader.nextLine();
            String[] parts = data.split(" ");
            int currentSrc = Integer.parseInt(parts[0]);
            if (currentSrc != src) {
                offset[src] = counter;

                src = currentSrc;
                counter = 0;
            }

            counter++;

            adjacencyList[SRCNODE][i] = Integer.parseInt(parts[0]);
            adjacencyList[TARGETNODE][i] = Integer.parseInt(parts[1]);
            adjacencyList[WEIGHT][i] = Integer.parseInt(parts[2]);

        }

        graphFileReader.close();
    }

    @Override
    public String toString() {
        return String.format("""
                    Graph (File: \"%s\")
                        Nodes: %d
                        Edges: %d
                """,
                graphFilePath, n, m);
    }
}
