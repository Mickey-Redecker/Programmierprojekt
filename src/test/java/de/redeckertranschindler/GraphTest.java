package de.redeckertranschindler;

import static de.redeckertranschindler.Graph.SRCNODE;
import static de.redeckertranschindler.Graph.TARGETNODE;
import static de.redeckertranschindler.Graph.WEIGHT;
import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.redeckertranschindler.util.Distance;
import de.redeckertranschindler.util.Point;

@TestMethodOrder(OrderAnnotation.class)
public class GraphTest {

    // String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Danny
    // String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Mickey
    String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Simon

    static Stream<Arguments> fixPointProvider() {
        return Stream.of(
                Arguments.of(50.03645, 9.34587),
                Arguments.of(51.0897525, 10.34453587),
                Arguments.of(49.03456645, 8.3454487),
                Arguments.of(52.03777455, 9.34456587));
    }

    private static Graph graph;
    private static QuadTree tree;

    @Test
    @Order(1)
    @Timeout(value = 90, unit = TimeUnit.SECONDS)
    public void loadGraph() {
        assertTrue(new File(graphFilePath).isFile(), "Graph-Datei existiert nicht!");

        try {
            graph = new Graph(graphFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(graph);

        double[][] nodes = graph.getNodes();
        int n = nodes[0].length;
        for (int i = 0; i < n; i++) {
            assertTrue(nodes[X][i] != 0);
            assertTrue(nodes[Y][i] != 0);
        }

        int[][] edges = graph.getEdges();
        for (int i = 0; i < edges[0].length; i++) {
            assertTrue(edges[SRCNODE][i] >= 0 && edges[SRCNODE][i] < n);
            assertTrue(edges[TARGETNODE][i] >= 0 && edges[TARGETNODE][i] < n);
            assertTrue(edges[WEIGHT][i] >= 0);
        }
    }

    @Test
    @Order(2)
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void generateQuadTree() {
        assumeTrue(graph != null);

        tree = new QuadTree(graph);

        assertNotNull(tree);
    }

    @ParameterizedTest
    @Order(3)
    @MethodSource("fixPointProvider")
    @Timeout(value = 1, unit = TimeUnit.SECONDS) // Hat overhead wegen Bruteforce-Check!
    public void closestPointFix(final double x, final double y) {
        assumeTrue(graph != null);
        assumeTrue(tree != null);

        Point p = new Point(x, y);
        int closestNode = tree.getClosestNode(p);
        assertTrue(0 <= closestNode && graph.getNodes()[0].length > closestNode);

        System.out.println(closestNode);
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

    @RepeatedTest(40)
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
