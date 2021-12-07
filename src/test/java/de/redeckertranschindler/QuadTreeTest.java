package de.redeckertranschindler;

import org.junit.Test;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class QuadTreeTest {

    @Test
    public void testTree1() {

        double[][] nodes = new double[2][10];
        double minX = 2;
        double maxX = 10;
        double minY = 2;
        double maxY = 10;

        nodes[X][0] = 2;
        nodes[Y][0] = 2;

        nodes[X][1] = 3;
        nodes[Y][1] = 3;

        nodes[X][2] = 2;
        nodes[Y][2] = 3;

        nodes[X][3] = 3;
        nodes[Y][3] = 2;

        nodes[X][4] = 4;
        nodes[Y][4] = 4;

        nodes[X][5] = 3;
        nodes[Y][5] = 4;

        nodes[X][6] = 4;
        nodes[Y][6] = 3;

        nodes[X][7] = 2;
        nodes[Y][7] = 4;

        nodes[X][8] = 4;
        nodes[Y][8] = 2;

        nodes[X][9] = 5;
        nodes[Y][9] = 5;

        double halfDimensionX = maxX - minX / 2;
        double halfDimensionY = maxY - minY / 2;

        double halfDimension = halfDimensionX > halfDimensionY ? halfDimensionX : halfDimensionY;

        // QuadTree g = new QuadTree(new Rectangle(new Point(minX + halfDimension, minY
        // + halfDimension), halfDimension),
        // nodes);

        // ArrayList<Integer> points = new ArrayList<>();
        // int id = g.getClosestNode(new Point(2.1, 5));

        // System.out.println("point: " + id + " x: " + nodes[X][id] + " y: " +
        // nodes[Y][id]);

    }

    @Test
    public void testTreeGermany() {
        double pX = 49.82;
        double pY = 8.8080;

        try {
            long time = System.currentTimeMillis();
            Graph g = new Graph("E:\\Programmierprojekt\\germany.fmi");
            System.out.println(System.currentTimeMillis() - time);
            double[][] nodes = g.getNodes();
            time = System.currentTimeMillis();
            QuadTree t = new QuadTree(g);

            System.out.println(System.currentTimeMillis() - time);
            time = System.currentTimeMillis();
            int id = t.getClosestNode(new Point(pX, pY));
            System.out.println(System.currentTimeMillis() - time);
            time = System.currentTimeMillis();

            System.out.println("point: " + id + " x: " + nodes[X][id] + " y: " +
                    nodes[Y][id]);

            int idB = -1;
            double distanceB = Double.MAX_VALUE;
            for (int i = 0; i < nodes[0].length; i++) {
                double d = Math.sqrt(Math.pow(pX - nodes[0][i], 2) + Math.pow(pY - nodes[1][i], 2));
                if (distanceB > d) {
                    distanceB = d;
                    idB = i;
                }
            }
            System.out.println(idB + " - " + id);
            System.out.println(
                    distanceB + " - " + Math.sqrt(Math.pow(pX - nodes[0][id], 2) + Math.pow(pY - nodes[1][id], 2)));

            assertTrue(idB == id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTree2() {

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

        // double halfDimension = halfDimensionX > halfDimensionY ? halfDimensionX :
        // halfDimensionY;

        // QuadTree g = new QuadTree(new Rectangle(new Point(minX + halfDimension, minY
        // + halfDimension), halfDimension),
        // nodes);

        // ArrayList<Integer> points = new ArrayList<>();
        // g.queryRange(new Rectangle(new Point(0.5, 0.5), 0.10), points);

        // System.out.println(points.toString());

        // System.out.println("Point: x=" + nodes[X][id] + ", y=" + nodes[Y][id]);
    }

}
