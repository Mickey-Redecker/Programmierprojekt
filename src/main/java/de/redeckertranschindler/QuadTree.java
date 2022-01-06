package de.redeckertranschindler;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;
import static de.redeckertranschindler.util.Distance.distance;

import java.util.ArrayList;
import java.util.List;

import de.redeckertranschindler.util.Point;
import de.redeckertranschindler.util.Rectangle;

/**
 * Quadtree implementation for a given graph.
 * 
 * Quadtree gets fully generated in the constructor and is ready to use after.
 * 
 * You can quickly find the closest point in this graph to a given coordinate.
 */
public class QuadTree {

    private static final int CAPACITY = 10000;

    private final double[][] coordinates;

    private final Rectangle boundary;
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
            final QuadTree[] children = createQuadTree(elements);

            this.northWest = children[0];
            this.northEast = children[1];
            this.southEast = children[2];
            this.southWest = children[3];
        }
    }

    /**
     *creates a Quadtree given a list with elements(coordinates) and
     *the elements are distributed recursively to four child nodes
     *
     * @param elements
     * @return
     */
    private QuadTree[] createQuadTree(final List<Integer> elements) {

        final Point center = boundary.getCenter();
        final double halfDimension = boundary.getHalfDimension();
        final double nextDimension = halfDimension / 2;

        final Rectangle topLeftBox = new Rectangle(
                new Point(center.getX() - nextDimension, center.getY() + nextDimension),
                nextDimension);
        final Rectangle topRightBox = new Rectangle(
                new Point(center.getX() + nextDimension, center.getY() + nextDimension),
                nextDimension);
        final Rectangle bottomLeftBox = new Rectangle(
                new Point(center.getX() - nextDimension, center.getY() - nextDimension),
                nextDimension);
        final Rectangle bottomRightBox = new Rectangle(
                new Point(center.getX() + nextDimension, center.getY() - nextDimension),
                nextDimension);

        final List<Integer> nw = new ArrayList<Integer>();
        final List<Integer> ne = new ArrayList<Integer>();
        final List<Integer> sw = new ArrayList<Integer>();
        final List<Integer> se = new ArrayList<Integer>();

        // Check coordinates belongs to which partition
        for (final Integer i : elements) {
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

        final QuadTree[] res = new QuadTree[4];

        res[0] = new QuadTree(nw, topLeftBox, coordinates);
        res[1] = new QuadTree(ne, topRightBox, coordinates);
        res[2] = new QuadTree(se, bottomRightBox, coordinates);
        res[3] = new QuadTree(sw, bottomLeftBox, coordinates);

        return res;

    }

    /**
     *Performs a two-dimensional range query and adds all elements in the
     *selected area to the result list.
     *
     * @param results
     * @param range
     */
    private void rangeQuery(final List<Integer> results, final Rectangle range) {
        if (this.boundary.intersects(range)) {

            if (this.points == null) {
                northWest.rangeQuery(results, range);
                northEast.rangeQuery(results, range);
                southWest.rangeQuery(results, range);
                southEast.rangeQuery(results, range);
            } else {

                for (final Integer i : points) {
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
        final double s = 0.1;
        Rectangle range = new Rectangle(p, s);

        double minDistance = Double.MAX_VALUE;
        int n = -1;

        while (true) {

            final List<Integer> results = new ArrayList<>();
            rangeQuery(results, range);
            for (final Integer i : results) {
                final double currentDistance = distance(p, i);
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
    }
}
