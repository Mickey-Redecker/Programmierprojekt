package de.redeckertranschindler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class QuadTree {

    private static final int CAPACITY = 500;

    private final double[][] coordinates;

    private Rectangle boundary;
    private List<Integer> points;

    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;

    public QuadTree(final Graph g) {
        this.boundary = g.getBoundary();
        this.coordinates = g.getNodes();
        List<Integer> elements = new LinkedList<>();

        for (int i = 0; i < coordinates[0].length; i++) {
            elements.add(i);
        }

        createQuadTree(elements);
    }

    public QuadTree(final List<Integer> list, final Rectangle boundary, final double[][] nodes) {
        this.boundary = boundary;
        this.coordinates = nodes;
        createQuadTree(list);
    }

    private void createQuadTree(final List<Integer> list) {
        if (CAPACITY > list.size()) {
            this.points = list;
        } else {
            Point center = boundary.getCenter();
            double halfDimension = boundary.getHalfDimension();
            double nextDimension = halfDimension / 2;

            Rectangle topLeftBox = new Rectangle(
                    new Point(center.getX() - nextDimension, center.getY() + nextDimension),
                    nextDimension);
            Rectangle topRightBox = new Rectangle(
                    new Point(center.getX() + nextDimension, center.getY() + nextDimension),
                    nextDimension);
            Rectangle bottomLeftBox = new Rectangle(
                    new Point(center.getX() - nextDimension, center.getY() - nextDimension),
                    nextDimension);
            Rectangle bottomRightBox = new Rectangle(
                    new Point(center.getX() + nextDimension, center.getY() - nextDimension),
                    nextDimension);

            this.northWest = createSubTree(list, topLeftBox);
            this.northEast = createSubTree(list, topRightBox);
            this.southEast = createSubTree(list, bottomRightBox);
            this.southWest = createSubTree(list, bottomLeftBox);
        }
    }

    public QuadTree createSubTree(final List<Integer> list, final Rectangle boundary) {
        final List<Integer> elements = new LinkedList<>();
        for (final Integer i : list) {
            if (boundary.containsPoint(new Point(coordinates[X][i], coordinates[Y][i]))) {
                elements.add(i);
            }
        }

        return new QuadTree(elements, boundary, coordinates);
    }

    public void rangeQuery(final List<Integer> results, final Rectangle range) {
        if (this.boundary.intersects(range)) {

            if (this.points == null) {
                northWest.rangeQuery(results, range);
                northEast.rangeQuery(results, range);
                southWest.rangeQuery(results, range);
                southEast.rangeQuery(results, range);
            } else {

                for (Integer i : points) {
                    if (range.containsPoint(new Point(coordinates[X][i], coordinates[Y][i]))) {
                        results.add(i);
                    }
                }
            }
        }
    }

    private double distance(final int node1, final int node2) {
        return distance(coordinates[X][node1], coordinates[Y][node1], coordinates[X][node2], coordinates[Y][node2]);
    }

    private double distance(final double x1, final double y1, final int node2) {
        return distance(x1, y1, coordinates[X][node2], coordinates[Y][node2]);
    }

    private double distance(final double x1, final double y1, final Point p2) {
        return distance(x1, y1, p2.getX(), p2.getY());
    }

    private double distance(final Point p1, final int node2) {
        return distance(p1.getX(), p1.getY(), coordinates[X][node2], coordinates[Y][node2]);
    }

    private double distance(final Point p1, final Point p2) {
        return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    private double distance(final double x1, final double y1, final double x2, final double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public int getClosestNode(final Point p) {
        double s = 0.1;
        Rectangle range = new Rectangle(p, s);

        int nodeId = -1;
        double minDistance = Double.MAX_VALUE;
        int n = -1;

        while (nodeId < 0) {

            List<Integer> results = new ArrayList<>();
            rangeQuery(results, range);
            System.out.println("Amount of points in range: " + results.size());
            for (Integer i : results) {
                double currentDistance = distance(p, i);
                if (minDistance > currentDistance) {
                    n = i;
                    minDistance = currentDistance;
                }
            }

            if (!(n == -1) && distance(p, n) < range.getHalfDimension()) {
                return n;
            } else {
                range = new Rectangle(p, range.getHalfDimension() + s);
            }
        }

        return nodeId;
    }
}
