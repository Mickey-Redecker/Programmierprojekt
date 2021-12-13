package de.redeckertranschindler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * @throws IOException
     */
    public Graph(final String graphFilePath) throws IOException {

        this.graphFilePath = graphFilePath;
        final FileReader graphFileReader = new FileReader(graphFilePath);
        final BufferedReader graphReader = new BufferedReader(graphFileReader);

        for (int i = 0; i < 5; i++) {
            graphReader.readLine();
        }

        n = Integer.parseInt(graphReader.readLine());
        m = Integer.parseInt(graphReader.readLine());

        coordinates = new double[2][n];
        offset = new int[n];
        adjacencyList = new int[3][m];

        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            final String data = graphReader.readLine();
            final String[] parts = data.split(" ");

            final int id = Integer.parseInt(parts[0]);
            final double x = Double.parseDouble(parts[2]);
            final double y = Double.parseDouble(parts[3]);
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

            final String data = graphReader.readLine();
            final String[] parts = data.split(" ");
            final int currentSrc = Integer.parseInt(parts[0]);
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

        graphReader.close();
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

    public double oneToOneDijkstra(final int startId, final int endId) {
        return 0d;
    }

    public double[] oneToAllDijkstra(final int startId) {
        return new double[n];
    }

    public Rectangle getBoundary() {
        final double width = maxX - minX;
        final double height = maxY - minY;

        final double dimension = width > height ? width : height;
        final double halfDimension = dimension / 2;

        return new Rectangle(new Point(minX + halfDimension, minY + halfDimension), halfDimension);
    }

    public double[][] getNodes() {
        return coordinates;
    }

    public int[][] getEdges() {
        return adjacencyList;
    }

    public List<Integer> getIdList() {
        final List<Integer> elements = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            elements.add(i);
        }

        return elements;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }
}
