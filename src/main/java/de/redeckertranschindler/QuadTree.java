package de.redeckertranschindler;

import java.util.ArrayList;
import java.util.List;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class QuadTree {

    private static final int CAPACITY = 10;

    private final double[][] coordinates;

    private Rectangle boundary;
    private ArrayList<Integer> points;

    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;

    public QuadTree(final Rectangle boundary, final double[][] nodes) {
        this.boundary = boundary;
        points = new ArrayList<>();
        northWest = null;
        northEast = null;
        southWest = null;
        southEast = null;
        this.coordinates = nodes;
    }

    public void insert(final Point p, final int id) {
        if (!boundary.containsPoint(p))
            System.err.println("Point out of boundary");

        if (points.size() < CAPACITY && northWest == null) {
            points.add(id);
            return;
        }

        if (northWest == null)
            subdivide();

        northWest.insert(p, id);
        northEast.insert(p, id);
        southWest.insert(p, id);
        southEast.insert(p, id);

    }

    public void subdivide() {
        double dimension = this.boundary.getHalfDimension() / 2;
        double centerX = this.boundary.getCenter().getX();
        double centerY = this.boundary.getCenter().getY();

        northWest = new QuadTree(new Rectangle(new Point(centerX - dimension, centerY + dimension), dimension),
                coordinates);
        northEast = new QuadTree(new Rectangle(new Point(centerX + dimension, centerY + dimension), dimension),
                coordinates);
        southWest = new QuadTree(new Rectangle(new Point(centerX - dimension, centerY - dimension), dimension),
                coordinates);
        southEast = new QuadTree(new Rectangle(new Point(centerX + dimension, centerY - dimension), dimension),
                coordinates);
    }

    public void queryRange(final Rectangle range, List<Integer> results) {
        if (!boundary.intersects(range)) {
            return;
        }

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != null) {
                double x = coordinates[X][i];
                double y = coordinates[Y][i];

                if (range.containsPoint(new Point(x, y))) {
                    results.add(points.get(i));
                }
            }
        }

        if (northWest == null) {
            return;
        }

        northWest.queryRange(range, results);
        northEast.queryRange(range, results);
        southEast.queryRange(range, results);
        southWest.queryRange(range, results);
    }

}
