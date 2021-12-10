package de.redeckertranschindler.util;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class Distance {

    private static double[][] coordinates;

    public static void setCoordinates(final double[][] coordinates) {
        Distance.coordinates = coordinates;
    }

    public static final double distance(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static final double distance(final Point p1, final int node2) {
        return distance(p1.getX(), p1.getY(), coordinates[X][node2], coordinates[Y][node2]);
    }

    public static final double distance(final double x1, final double y1, final int node2) {
        return distance(x1, y1, coordinates[X][node2], coordinates[Y][node2]);
    }

}
