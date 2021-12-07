package de.redeckertranschindler;

import org.junit.Test;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

import java.util.ArrayList;

public class QuadTreeTest {

    @Test
    public void testTree1() {

        double[][] nodes = new double[2][10];
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (int i = 0; i < nodes[0].length; i++) {
            double valueX = Math.random();
            double valueY = Math.random();
            if (valueX > maxX) {
                maxX = valueX;
            }
            if (valueX < minX) {
                minX = valueX;
            }
            if (valueY > maxY) {
                maxY = valueY;
            }
            if (valueY < minY) {
                minY = valueY;
            }
            nodes[X][i] = Math.random();
            nodes[Y][i] = Math.random();
        }

        System.out.println(minX + ", " + maxX + ", " + minY + ", " + maxY);

        double halfDimensionX = maxX - minX / 2;
        double halfDimensionY = maxY - minY / 2;

        double halfDimension = halfDimensionX > halfDimensionY ? halfDimensionX : halfDimensionY;

        QuadTree g = new QuadTree(new Rectangle(new Point(minX + halfDimension, minY + halfDimension), halfDimension),
                nodes);

        ArrayList<Integer> points = new ArrayList<>();
        g.queryRange(new Rectangle(new Point(0.5, 0.5), 0.10), points);

        System.out.println(points.toString());

        // System.out.println("Point: x=" + nodes[X][id] + ", y=" + nodes[Y][id]);
    }

}
