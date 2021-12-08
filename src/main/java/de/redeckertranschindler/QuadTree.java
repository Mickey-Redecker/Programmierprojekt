package de.redeckertranschindler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.redeckertranschindler.util.Point;
import de.redeckertranschindler.util.Rectangle;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;
import static de.redeckertranschindler.util.Distance.distance;

/**
 * Quadtree implementation for a given graph.
 * 
 * Quadtree gets fully generated in the constructor and ist ready to use after.
 * 
 * You can quickly find the closest point in this graph to a given coordinate.
 */
public class QuadTree {

    private static final int CAPACITY = 1000;

    private final double[][] coordinates;

    private Rectangle boundary;
    private List<Integer> points;

    private final QuadTree northWest;
    private final QuadTree northEast;
    private final QuadTree southWest;
    private final QuadTree southEast;

    public QuadTree(final Graph g) {
        this(g.getIdList(), g.getBoundary(), g.getNodes());
    }

    private QuadTree(final List<Integer> elements, final Rectangle boundary, final double[][] nodes) {
        this.boundary = boundary;
        this.coordinates = nodes;

        if (CAPACITY > elements.size()) {
            this.points = elements;
            this.northWest = null;
            this.northEast = null;
            this.southEast = null;
            this.southWest = null;

        } else {
            QuadTree[] children = createQuadTree(elements);

            this.northWest = children[0];
            this.northEast = children[1];
            this.southEast = children[2];
            this.southWest = children[3];
        }
    }

    private QuadTree[] createQuadTree(final List<Integer> elements) {

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

        List<Integer> nw = new LinkedList<Integer>();
        List<Integer> ne = new LinkedList<Integer>();
        List<Integer> sw = new LinkedList<Integer>();
        List<Integer> se = new LinkedList<Integer>();

        for (Integer i : elements) {
            if (coordinates[X][i] < center.getX()) {
                if (coordinates[Y][i] < center.getY()) {
                    sw.add(i);
                } else {
                    nw.add(i);
                }
            } else {
                if (coordinates[Y][i] < center.getY()) {
                    se.add(i);
                } else {
                    ne.add(i);
                }
            }
        }

        QuadTree[] res = new QuadTree[4];

        res[0] = new QuadTree(nw, topLeftBox, coordinates);
        res[1] = new QuadTree(ne, topRightBox, coordinates);
        res[2] = new QuadTree(se, bottomRightBox, coordinates);
        res[3] = new QuadTree(sw, bottomLeftBox, coordinates);

        return res;

    }

    private void rangeQuery(final List<Integer> results, final Rectangle range) {
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

    /**
     * Calculates quickly the nearest neighbour of the given coordinates
     * 
     * @param p
     * @return id of the closest point
     */
    public int getClosestNode(final Point p) {
        double s = 0.1;
        Rectangle range = new Rectangle(p, s);

        int nodeId = -1;
        double minDistance = Double.MAX_VALUE;
        int n = -1;

        while (nodeId < 0) {

            List<Integer> results = new ArrayList<>();
            rangeQuery(results, range);
            for (Integer i : results) {
                double currentDistance = distance(p, i);
                if (minDistance > currentDistance) {
                    n = i;
                    minDistance = currentDistance;
                }
            }

            if ((!(n == -1)) && (distance(p, n) < range.getHalfDimension())) {
                return n;
            } else {
                range = new Rectangle(p, range.getHalfDimension() + s);
            }
        }

        return nodeId;
    }
}
