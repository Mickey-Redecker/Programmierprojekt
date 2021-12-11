package de.redeckertranschindler;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import de.redeckertranschindler.util.Distance;
import de.redeckertranschindler.util.Point;

@TestMethodOrder(OrderAnnotation.class)
public class GraphTest {

    // String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Danny
    // String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Mickey
    String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Simon

    private static Graph graph;
    private static QuadTree tree;

    @Test
    @Order(1)
    @Timeout(value = 90, unit = TimeUnit.SECONDS)
    public void loadGraph() {
        assertTrue(new File(graphFilePath).isFile(), "Graph-Datei existiert nicht!");

        try {
            graph = new Graph(graphFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertNotNull(graph);
    }

    @Test
    @Order(2)
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void generateQuadTree() {
        assumeTrue(graph != null);

        tree = new QuadTree(graph);
    }

    @Test
    @Order(3)
    @Timeout(value = 1, unit = TimeUnit.SECONDS) // Hat overhead wegen Bruteforce-Check!
    public void closestPointFix() {
        assumeTrue(graph != null);
        assumeTrue(tree != null);

        double x = 51.168;
        double y = 9.0156;
        Point p = new Point(x, y);
        int closestNode = tree.getClosestNode(p);
        assertTrue(0 <= closestNode && graph.getNodes()[0].length > closestNode);

        double[][] nodes = graph.getNodes();
        double distance = Double.MAX_VALUE;
        int id = -1;
        for (int i = 0; i < nodes[0].length; i++) {
            double currentDistance = Distance.distance(p, i);
            if (currentDistance < distance) {
                distance = currentDistance;
                id = i;
            }
        }

        assertTrue(id == closestNode);
    }

    @RepeatedTest(20)
    @Order(5)
    @Timeout(value = 1, unit = TimeUnit.SECONDS) // Hat overhead wegen Bruteforce-Check!
    public void closestPointRandom() {
        assumeTrue(graph != null);
        assumeTrue(tree != null);

        double x = graph.getMinX() + (Math.random() * (graph.getMaxX() - graph.getMinX()));
        double y = graph.getMinY() + (Math.random() * (graph.getMaxY() - graph.getMinY()));
        Point p = new Point(x, y);
        int closestNode = tree.getClosestNode(p);
        assertTrue(0 <= closestNode && graph.getNodes()[0].length > closestNode);

        double[][] nodes = graph.getNodes();
        double distance = Double.MAX_VALUE;
        int id = -1;
        for (int i = 0; i < nodes[0].length; i++) {
            double currentDistance = Distance.distance(p, i);
            if (currentDistance < distance) {
                distance = currentDistance;
                id = i;
            }
        }

        assertTrue(id == closestNode);
    }

}
