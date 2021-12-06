package de.redeckertranschindler;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class Grid {

    private final ArrayList[][] grid;

    private final double cellSize;

    private final int cellAmountX = 200;
    private final int cellAmountY;

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    private double[][] coordinates;

    public Grid(final double minX, final double maxX, final double minY, final double maxY, final double[][] nodes) {

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        double width = (maxX - minX);
        double height = (maxY - minY);

        // size of an edge of a cell with square cells
        cellSize = width / cellAmountX;

        cellAmountY = (int) Math.ceil(height / cellAmountX);

        grid = new ArrayList[cellAmountX][cellAmountY];

        this.coordinates = nodes;

        insertNodes();
    }

    private void insertNodes() {

        for (int i = 0; i < coordinates.length; i++) {
            int cellX = (int) Math.floor((coordinates[X][i] - minX) / cellSize);
            int cellY = (int) Math.floor((coordinates[Y][i] - minY) / cellSize);

            grid[cellX][cellY].add(i);
        }
    }

    private double distance(final int node1, final int node2) {
        return distance(coordinates[X][node1], coordinates[Y][node1], coordinates[X][node2], coordinates[Y][node2]);
    }

    private double distance(final double x1, final double y1, final int node2) {
        return distance(x1, y1, coordinates[X][node2], coordinates[Y][node2]);
    }

    private double distance(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    // public int getClosestNodeId(final double x, final double y) {

    // int closestNodeId = -1;
    // double closestDistance = Double.MAX_VALUE;

    // int cellX = (int) Math.floor((x - minX) / cellSize);
    // int cellY = (int) Math.floor((y - minY) / cellSize);

    // for (int i = 0; i < grid[cellX][cellY].size(); i++) {
    // int nodeId = (int) grid[cellX][cellY].get(i);
    // double distance = distance(x, y, nodeId);

    // if (closestDistance > distance) {
    // closestNodeId = nodeId;
    // closestDistance = distance;
    // }
    // }

    // if (closestNodeId == -1) {
    // System.err.println("Kein Knoten im Graph!");
    // }
    // return closestNodeId;
    // }

    public void getClosestNode(final double x, final double y) {
        int cellX = (int) Math.floor((x - minX) / cellSize);
        int cellY = (int) Math.floor((y - minY) / cellSize);
        getClosestNode(0, x, y, new Cell(cellX, cellY));
    }

    private class Cell {
        private int x;
        private int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getCellX() {
            return this.x;
        }

        public int getCellY() {
            return this.y;
        }
    }

    private void getClosestNode(final int cellDistance, final double x, final double y, Cell sourceCell) {

        Set<Cell> cellsToSearch = new TreeSet<Cell>();

    }

}
