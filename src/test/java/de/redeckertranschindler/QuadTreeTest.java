package de.redeckertranschindler;

import java.io.IOException;

import org.junit.Test;

import de.redeckertranschindler.util.Distance;
import de.redeckertranschindler.util.Point;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

import static org.junit.Assert.assertTrue;

public class QuadTreeTest {

    @Test
    public void testGermanyRandompoint() {
        double pX = 0d;
        double pY = 0d;

        long time = 0l;
        long timeReadFile = 0l;
        long timeQuadTree = 0l;
        long timeClosestPoint = 0l;

        try {
            time = System.currentTimeMillis();
            Graph g = new Graph("E:\\Programmierprojekt\\germany.fmi");
            timeReadFile = System.currentTimeMillis() - time;
            System.out.println("Read File: " + timeReadFile + " ms");

            pX = g.getMinX() + (Math.random() * (g.getMaxX() - g.getMinX()));
            pY = g.getMinY() + (Math.random() * (g.getMaxY() - g.getMinY()));

            time = System.currentTimeMillis();
            QuadTree t = new QuadTree(g);
            timeQuadTree = System.currentTimeMillis() - time;
            System.out.println("Generate Quadtree: " + timeQuadTree + " ms");

            time = System.currentTimeMillis();
            int id = t.getClosestNode(new Point(pX, pY));
            timeClosestPoint = System.currentTimeMillis() - time;
            System.out.println("Find Closest Point: " + timeClosestPoint + " ms");

            assertTrue("> 90 Sekunden gebraucht um die Graphdatei einzulesen!", timeReadFile < 90000);
            assertTrue("> 1 Sekunde gebraucht um den nächsten Punkt zufinden!", timeClosestPoint < 1000);

            double[][] nodes = g.getNodes();
            System.out.println("Node: " + id + "\nx: " + nodes[X][id] + " y: " +
                    nodes[Y][id]);

            int idB = -1;
            double distanceB = Double.MAX_VALUE;
            for (int i = 0; i < nodes[0].length; i++) {
                double d = Distance.distance(pX, pY, i);
                if (distanceB > d) {
                    distanceB = d;
                    idB = i;
                }
            }

            if (idB != id) {
                System.out.println(idB + " - " + id);
                System.out.println(
                        distanceB + " - " + Math.sqrt(Math.pow(pX - nodes[0][id], 2) + Math.pow(pY - nodes[1][id], 2)));
            }
            assertTrue("Bruteforce fand einen anderen näheren Punkt!", idB == id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGermanyFixpoint() {
        double pX = 49.82;
        double pY = 8.8080;
        long time = 0l;
        long timeReadFile = 0l;
        long timeQuadTree = 0l;
        long timeClosestPoint = 0l;

        try {
            time = System.currentTimeMillis();
            Graph g = new Graph("E:\\Programmierprojekt\\germany.fmi");
            timeReadFile = System.currentTimeMillis() - time;
            System.out.println("Read File: " + timeReadFile + " ms");

            time = System.currentTimeMillis();
            QuadTree t = new QuadTree(g);
            timeQuadTree = System.currentTimeMillis() - time;
            System.out.println("Generate Quadtree: " + timeQuadTree + " ms");

            time = System.currentTimeMillis();
            int id = t.getClosestNode(new Point(pX, pY));
            timeClosestPoint = System.currentTimeMillis() - time;
            System.out.println("Find Closest Point: " + timeClosestPoint + " ms");

            assertTrue("> 90 Sekunden gebraucht um die Graphdatei einzulesen!", timeReadFile < 90000);
            assertTrue("> 1 Sekunde gebraucht um den nächsten Punkt zufinden!", timeClosestPoint < 1000);

            double[][] nodes = g.getNodes();
            System.out.println("Node: " + id + "\nx: " + nodes[X][id] + " y: " +
                    nodes[Y][id]);

            int idB = -1;
            double distanceB = Double.MAX_VALUE;
            for (int i = 0; i < nodes[0].length; i++) {
                double d = Distance.distance(pX, pY, i);
                if (distanceB > d) {
                    distanceB = d;
                    idB = i;
                }
            }

            if (idB != id) {
                System.out.println(idB + " - " + id);
                System.out.println(
                        distanceB + " - " + Math.sqrt(Math.pow(pX - nodes[0][id], 2) + Math.pow(pY - nodes[1][id], 2)));
            }
            assertTrue("Bruteforce fand einen anderen näheren Punkt!", idB == id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
