package de.redeckertranschindler.util;

public class Rectangle {
    private final Point center;
    private final double halfDimension;

    public Rectangle(final Point center, final double halfDimension) {
        this.center = center;
        this.halfDimension = halfDimension;
    }

    public boolean containsPoint(final Point p) {
        return (p.getX() >= center.getX() - halfDimension
                && p.getX() < center.getX() + halfDimension
                && p.getY() >= center.getY() - halfDimension
                && p.getY() < center.getY() + halfDimension);
    }

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
