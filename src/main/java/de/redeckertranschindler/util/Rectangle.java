package de.redeckertranschindler.util;

/**
 * This class represents a quadrilateral, where the angles are 90 degrees.
 */

public class Rectangle {
    private final Point center;
    private final double halfDimension;

    public Rectangle(final Point center, final double halfDimension) {
        this.center = center;
        this.halfDimension = halfDimension;
    }

    /**
     * Checks whether a given point is inside this rectangle.
     *
     * The point is inside the rectangle if all it is on the corners, edges or
     * inside the rectangle.
     * @param p the point to check
     * @return true if the point is inside the rectangle false if not
     */


    public boolean containsPoint(final Point p) {
        return (p.getX() >= center.getX() - halfDimension
                && p.getX() < center.getX() + halfDimension
                && p.getY() >= center.getY() - halfDimension
                && p.getY() < center.getY() + halfDimension);
    }

    /**
     * Checks whether a given rectangle intersects this rectangle.
     *
     * The given rectangle intersects this rectangle if it is (1) inside this
     * rectangle, (2) both rectangles have an overlapping part inside, or (3) both
     * rectangles overlap on the edge.
     *
     * @param rect the rectangle to check
     * @return true if the rectangle intersects this rectangle else false

     */


    public boolean intersects(final Rectangle rect) {
        return !(rect.center.getX() + rect.halfDimension < this.center.getX() - this.halfDimension
                || rect.center.getX() - rect.halfDimension > this.center.getX() + this.halfDimension
                || rect.center.getY() + rect.halfDimension < this.center.getY() - this.halfDimension
                || rect.center.getY() - rect.halfDimension > this.center.getY() + this.halfDimension);
    }

    public double getHalfDimension() {
        return halfDimension;
    }

    public Point getCenter() {
        return center;
    }
}
