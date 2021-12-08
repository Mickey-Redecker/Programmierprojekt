package de.redeckertranschindler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import de.redeckertranschindler.util.Distance;
import de.redeckertranschindler.util.Point;
import de.redeckertranschindler.util.Rectangle;

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

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

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

        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            String data = graphFileReader.nextLine();
            String[] parts = data.split(" ");

            int id = Integer.parseInt(parts[0]);
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            coordinates[X][id] = x;
            coordinates[Y][id] = y;

            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
        }

        int src = 0;
        int counter = 0;

        for (int edgeID = 0; edgeID < m; edgeID++) {
            String data = graphFileReader.nextLine();
            String[] parts = data.split(" ");
            int currentSrc = Integer.parseInt(parts[0]);
            if (currentSrc != src) {
                offset[src] = counter;

                src = currentSrc;
                counter = 0;
            }

            counter++;

            adjacencyList[SRCNODE][edgeID] = Integer.parseInt(parts[0]);
            adjacencyList[TARGETNODE][edgeID] = Integer.parseInt(parts[1]);
            adjacencyList[WEIGHT][edgeID] = Integer.parseInt(parts[2]);

        }

        graphFileReader.close();
        Distance.setCoordinates(coordinates);
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

    public void dijkstra(final int startId, final int endId) {

    }

    public Rectangle getBoundary() {
        double width = maxX - minX;
        double height = maxY - minY;

        double dimension = width > height ? width : height;
        double halfDimension = dimension / 2;

        return new Rectangle(new Point(minX + halfDimension, minY + halfDimension), halfDimension);
    }

    public double[][] getNodes() {
        return coordinates;
    }

    public List<Integer> getIdList() {
        List<Integer> elements = new LinkedList<>();

        for (int i = 0; i < coordinates[0].length; i++) {
            elements.add(i);
        }

        return elements;
    }
}
