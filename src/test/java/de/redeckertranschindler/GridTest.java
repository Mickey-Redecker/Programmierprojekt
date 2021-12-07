package de.redeckertranschindler;

import org.junit.Test;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

public class GridTest {

    @Test
    public void testGrid1() {

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

        Grid g = new Grid(minX, maxX, minY, maxY, nodes);

        int id = g.getClosestNode(3, 3);

        System.out.println("Point: x=" + nodes[X][id] + ", y=" + nodes[Y][id]);

    }
}
