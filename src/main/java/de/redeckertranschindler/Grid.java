package de.redeckertranschindler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class Grid {

    private final ArrayList[][] grid;

    private final double cellSize;

    private final int cellAmountX = 2;
    private final int cellAmountY;

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    private double[][] coordinates;

    public Grid(final double minX, final double maxX, final double minY, final double maxY, final double[][] nodes) {

        this.minX = minX;
        this.maxX = maxX + 0.001;
        this.minY = minY;
        this.maxY = maxY + 0.001;

        double width = (this.maxX - this.minX);
        double height = (this.maxY - this.minY);

        // size of an edge of a cell with square cells
        cellSize = width / cellAmountX;

        cellAmountY = (int) Math.ceil(height / cellAmountX);

        grid = new ArrayList[cellAmountX][cellAmountY];

        this.coordinates = nodes;

        insertNodes();
    }

    private void insertNodes() {

        for (int i = 0; i < coordinates[0].length; i++) {
            int cellX = (int) Math.floor((coordinates[X][i] - minX) / cellSize);
            int cellY = (int) Math.floor((coordinates[Y][i] - minY) / cellSize);

            System.out.println(cellX + " " + cellY);

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

    public int getClosestNode(final double x, final double y) {
        int cellX = (int) Math.floor((x - minX) / cellSize);
        int cellY = (int) Math.floor((y - minY) / cellSize);
        return getClosestNode(x, y, new Cell(cellX, cellY));
    }

    private int getClosestNode(final double x, final double y, final Cell sourceCell) {

        // null in first (0th) iteration
        Set<Cell> furtherCells = null;

        int iteration = 0;

        // closestNode == -1 <==> no node found yet
        int closestNode = -1;
        // newDistance == Double.MAX_VALUE <==> no node found yet
        // distance between source and closest node found
        double previousDistance = Double.MAX_VALUE;

        while (true) {

            // search all nodes in Cell for closer node, only during first (0th) iteration
            if (furtherCells == null) {
                int numberNodesInCell = grid[sourceCell.getCellX()][sourceCell.getCellY()].size();
                for (int i = 0; i < numberNodesInCell; i++) {
                    double distance = distance(x, y, (int) grid[sourceCell.getCellX()][sourceCell.getCellY()].get(i));
                    if (distance < previousDistance) {
                        closestNode = (int) grid[sourceCell.getCellX()][sourceCell.getCellY()].get(i);
                        previousDistance = distance;
                    }
                }
            }
            // search all nodes in further cells for closer node, always unless in first
            // (0th) iteration
            else {
                for (Cell cell : furtherCells) {
                    // only execute for cells that are actually present in the grid
                    if (cellAmountX >= cell.getCellX() && cellAmountY >= cell.getCellY()) {
                        int numberNodesInCell = grid[cell.getCellX()][cell.getCellY()].size();
                        for (int i = 0; i < numberNodesInCell; i++) {
                            double distance = distance(x, y,
                                    (int) grid[sourceCell.getCellX()][sourceCell.getCellY()].get(i));
                            if (distance < previousDistance) {
                                closestNode = (int) grid[sourceCell.getCellX()][sourceCell.getCellY()].get(i);
                                previousDistance = distance;
                            }
                        }
                    }
                }
            }

            // decide in which directions furhter cells need to be searched
            boolean furtherSearchNorth = false;
            boolean furtherSearchSouth = false;
            boolean furtherSearchEast = false;
            boolean furtherSearchWest = false;

            double northBorderY = cellSize * (sourceCell.getCellY() + iteration + 1);
            double distanceToNorthBorder = northBorderY - y;
            if (distanceToNorthBorder < previousDistance) {
                furtherSearchNorth = true;
            }

            double southBorderY = cellSize * (sourceCell.getCellY() + iteration);
            double distanceToSouthBorder = southBorderY - y;
            if (distanceToSouthBorder < previousDistance) {
                furtherSearchSouth = true;
            }

            double eastBorderX = cellSize * (sourceCell.getCellX() + iteration + 1);
            double distanceToEastBorder = eastBorderX - x;
            if (distanceToEastBorder < previousDistance) {
                furtherSearchEast = true;
            }
            double westBorderX = cellSize * (sourceCell.getCellX() + iteration);
            double distanceToWestBorder = westBorderX - x;
            if (distanceToWestBorder < previousDistance) {
                furtherSearchWest = true;
            }

            // if no more cells need to be searched return closest node found, else continue
            // preparing and executing new iteration
            if (!furtherSearchNorth && !furtherSearchEast && !furtherSearchSouth && !furtherSearchWest) {
                return closestNode;
            }

            // decide which further cells need to be searched
            furtherCells = new HashSet<Cell>();

            // check corner cells
            if (furtherSearchNorth && furtherSearchEast) {
                // TODO: Add nort-east corner cell
                furtherCells
                        .add(new Cell(sourceCell.getCellX() + 1 + iteration, sourceCell.getCellY() + 1 + iteration));
            }

            if (furtherSearchEast && furtherSearchSouth) {
                furtherCells
                        .add(new Cell(sourceCell.getCellX() + 1 + iteration, sourceCell.getCellY() - 1 - iteration));
            }

            if (furtherSearchSouth && furtherSearchWest) {
                // TODO: Add south-west corner cell
                furtherCells
                        .add(new Cell(sourceCell.getCellX() - 1 - iteration, sourceCell.getCellY() - 1 - iteration));
            }

            if (furtherSearchWest && furtherSearchNorth) {
                furtherCells
                        .add(new Cell(sourceCell.getCellX() - 1 - iteration, sourceCell.getCellY() + 1 + iteration));
            }

            // check non-corner cells

            if (furtherSearchNorth) {
                for (int i = sourceCell.getCellX() - iteration; i <= sourceCell.getCellX() + iteration; i++) {
                    furtherCells.add(new Cell(i, sourceCell.getCellY() + 1 + iteration));
                }
            }

            if (furtherSearchSouth) {
                for (int i = sourceCell.getCellX() - iteration; i <= sourceCell.getCellX() + iteration; i++) {
                    furtherCells.add(new Cell(i, sourceCell.getCellY() - 1 - iteration));
                }
            }

            if (furtherSearchEast) {
                for (int i = sourceCell.getCellY() - iteration; i <= sourceCell.getCellY() + iteration; i++) {
                    furtherCells.add(new Cell(sourceCell.getCellX() + 1 + iteration, i));
                }
            }

            if (furtherSearchWest) {
                for (int i = sourceCell.getCellY() - iteration; i <= sourceCell.getCellY() + iteration; i++) {
                    furtherCells.add(new Cell(sourceCell.getCellX() - 1 - iteration, i));
                }
            }

            // furtherCells contains all further cells that need to be searched, start from
            // beginning with next iteration
            iteration++;

        }

    }

}
