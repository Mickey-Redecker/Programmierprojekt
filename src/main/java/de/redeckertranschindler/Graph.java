package de.redeckertranschindler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import de.redeckertranschindler.util.DijkstraResult;
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

        for (int edgeID = 0; edgeID < m; edgeID++) {
            final String data = graphReader.readLine();
            final String[] parts = data.split(" ");
            final int currentSrc = Integer.parseInt(parts[0]);
            if (currentSrc != src) {
                for (int i = src + 1; i <= currentSrc; i++) {
                    offset[i] = edgeID;
                }
                src = currentSrc;
            }

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

    public DijkstraResult oneToOneDijkstra(final int startId, final int endId) {

        final int[] distances = new int[n];
        final int[] previousNode = new int[n];
        final boolean[] finished = new boolean[n];

        final Queue<Integer> priorityQueue = new PriorityQueue<>(n, new Comparator<Integer>() {
            @Override
            public int compare(final Integer node1, final Integer node2) {
                return distances[node1] - distances[node2];
            }
        });

        previousNode[startId] = startId;
        for (int i = 0; i < n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[startId] = 0;
        priorityQueue.add(startId);

        while (!priorityQueue.isEmpty()) {

            final int srcNode = priorityQueue.poll();

            // Difference to OneToAll
            if (srcNode == endId) {
                break;
            }
            // ----------------------

            if (!finished[srcNode]) {

                finished[srcNode] = true;

                final int startOfEdges = offset[srcNode];
                final int endOfEdges = srcNode == n - 1 ? m : offset[srcNode + 1];

                for (int i = startOfEdges; i < endOfEdges; i++) {
                    final int weight = adjacencyList[WEIGHT][i];
                    final int targetNode = adjacencyList[TARGETNODE][i];

                    if (distances[targetNode] > distances[srcNode] + weight) {
                        distances[targetNode] = distances[srcNode] + weight;
                        previousNode[targetNode] = srcNode;

                        priorityQueue.add(targetNode);
                        continue;
                    }

                    if (!finished[targetNode]) {
                        priorityQueue.add(targetNode);
                    }

                }

            }
        }

        return new DijkstraResult(distances, previousNode, endId);
    }

    public DijkstraResult oneToAllDijkstra(final int startId) {

        final int[] distances = new int[n];
        final int[] previousNode = new int[n];
        final boolean[] finished = new boolean[n];

        final Queue<Integer> priorityQueue = new PriorityQueue<>(n, new Comparator<Integer>() {
            @Override
            public int compare(final Integer node1, final Integer node2) {
                return distances[node1] - distances[node2];
            }
        });

        previousNode[startId] = startId;
        for (int i = 0; i < n; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[startId] = 0;
        priorityQueue.add(startId);

        while (!priorityQueue.isEmpty()) {

            final int srcNode = priorityQueue.poll();

            if (!finished[srcNode]) {

                finished[srcNode] = true;

                final int startOfEdges = offset[srcNode];
                final int endOfEdges = srcNode == n - 1 ? m : offset[srcNode + 1];

                for (int i = startOfEdges; i < endOfEdges; i++) {
                    final int weight = adjacencyList[WEIGHT][i];
                    final int targetNode = adjacencyList[TARGETNODE][i];

                    if (distances[targetNode] > distances[srcNode] + weight) {
                        distances[targetNode] = distances[srcNode] + weight;
                        previousNode[targetNode] = srcNode;

                        priorityQueue.add(targetNode);
                        continue;
                    }

                    if (!finished[targetNode]) {
                        priorityQueue.add(targetNode);
                    }

                }

            }
        }

        return new DijkstraResult(distances, previousNode);

    }

    // public List<List<Integer>> getshortestPath(final int start, final int target)
    // {
    // List<List<Integer>> result = new ArrayList<>();

    // return result;
    // }

    public Rectangle getBoundary() {
        final double width = maxX - minX;
        final double height = maxY - minY;

        final double dimension = width > height ? width : height;
        final double halfDimension = dimension / 2;

        return new Rectangle(new Point(minX + halfDimension, minY + halfDimension), halfDimension);
    }

    public int getNodeAmount() {
        return n;
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
