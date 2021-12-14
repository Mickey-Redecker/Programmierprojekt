package de.redeckertranschindler;

import static de.redeckertranschindler.Graph.SRCNODE;
import static de.redeckertranschindler.Graph.TARGETNODE;
import static de.redeckertranschindler.Graph.WEIGHT;
import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

@TestMethodOrder(OrderAnnotation.class)
public class DijkstraTest {

    // String graphFilePath = "C:\\Users\\Danny
    // Tran\\Desktop\\Programmierprojekt\\germany.fmi"; // Danny

    // String graphFilePath = "D:\\other\\germany(1).fmi"; // Mickey

    String graphFilePath = "E:\\Programmierprojekt\\germany.fmi"; // Simon
    String toyGraphFilePath = "E:\\Programmierprojekt\\toy.fmi"; // Simon
    String germanyQue = "E:\\Programmierprojekt\\Benchs\\germany.que"; // Simon
    String germanySol = "E:\\Programmierprojekt\\Benchs\\germany.sol"; // Simon

    private static Graph graph;

    @Test
    @Order(1)
    @Timeout(value = 90, unit = TimeUnit.SECONDS)
    public void loadToyGraph() {
        assertTrue(new File(toyGraphFilePath).isFile(), "Graph-Datei existiert nicht!");

        try {
            graph = new Graph(toyGraphFilePath);
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

        System.out.println(graph.toString());
    }

    @Test
    @Order(2)
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    public void runToyDijkstra() {
        int[] distances = graph.oneToAllDijkstra(2);

        assertEquals(6, distances[0]);
        assertEquals(5, distances[1]);
        assertEquals(0, distances[2]);
        assertEquals(5, distances[3]);
        assertEquals(4, distances[4]);
    }

    @Test
    @Order(3)
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
    @Order(4)
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    public void runDijkstra() {
        int[] distances = graph.oneToAllDijkstra(8371825);

        assertEquals(648681, distances[16743651]);
        assertEquals(649433, distances[16743652]);
        assertEquals(666379, distances[16743653]);
        assertEquals(648777, distances[16743654]);
        assertEquals(649372, distances[16743655]);
        assertEquals(649304, distances[16743656]);
        assertEquals(648885, distances[16743657]);
        assertEquals(649227, distances[16743658]);
        assertEquals(649163, distances[16743659]);
        assertEquals(648996, distances[16743660]);
    }

    @Test
    @Order(5)
    public void runGermanyOneToOneDijkstra() throws FileNotFoundException, IOException {

        final FileReader queFileReader = new FileReader(germanyQue);
        final BufferedReader queReader = new BufferedReader(queFileReader);

        final FileReader solFileReader = new FileReader(germanySol);
        final BufferedReader solReader = new BufferedReader(solFileReader);

        int counter = 0;

        while (queReader.ready()) {

            final String line = queReader.readLine();
            final String[] parts = line.split(" ");
            final int start = Integer.valueOf(parts[0]);
            final int end = Integer.valueOf(parts[1]);
            final int expected = Integer.valueOf(solReader.readLine());

            final int res = graph.oneToOneDijkstra(start, end);

            System.out.println(++counter);

            assertEquals(expected, res);
        }

        queReader.close();
        solReader.close();
    }

}
