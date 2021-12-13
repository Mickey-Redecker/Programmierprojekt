package de.redeckertranschindler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import de.redeckertranschindler.util.Point;

public class Benchmark {

    public static void main(String[] args) {
        // read parameters (parameters are expected in exactly this order)
        String graphPath = args[1];
        double lon = Double.parseDouble(args[3]);
        double lat = Double.parseDouble(args[5]);
        String quePath = args[7];
        int sourceNodeId = Integer.parseInt(args[9]);

        // run benchmarks
        System.out.println("Reading graph file " + graphPath);
        long graphReadStart = System.currentTimeMillis();
        Graph g = null;
        try {
            g = new Graph(graphPath);
        } catch (IOException e1) {
            System.out.println("Couldn't find the given graph-file!!!");
            System.exit(1);
        }
        long graphReadEnd = System.currentTimeMillis();
        System.out.println("\tgraph read took " + (graphReadEnd - graphReadStart) + "ms");

        System.out.println("Generating quadtree, n = " + g.getNodes()[0].length);
        System.out.println("\ttime is not critical!");
        long generateTreeStart = System.currentTimeMillis();
        QuadTree tree = new QuadTree(g);
        long generateTreeEnd = System.currentTimeMillis();
        System.out.println("\tgenerating quadtree took " + (generateTreeEnd - generateTreeStart) + "ms");

        System.out.println("Finding closest node to coordinates " + lon + " " + lat);
        long nodeFindStart = System.currentTimeMillis();
        int nodeId = tree.getClosestNode(new Point(lon, lat));
        long nodeFindEnd = System.currentTimeMillis();
        System.out.println("\tfound node: " + nodeId);
        System.out.println("\tfinding node took " + (nodeFindEnd - nodeFindStart) + "ms");

        System.out.println("Running one-to-one Dijkstras for queries in .que file " + quePath);
        long queStart = System.currentTimeMillis();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quePath))) {
            String currLine;
            while ((currLine = bufferedReader.readLine()) != null) {
                int oneToOneSourceNodeId = Integer.parseInt(currLine.substring(0, currLine.indexOf(" ")));
                int oneToOneTargetNodeId = Integer.parseInt(currLine.substring(currLine.indexOf(" ") + 1));
                int oneToOneDistance = g.oneToOneDijkstra(oneToOneSourceNodeId, oneToOneTargetNodeId);
                System.out.println(oneToOneDistance);
            }
        } catch (IOException e) {
            System.out.println("Couldn't find the given que-file!!!");
            System.exit(1);
        }
        long queEnd = System.currentTimeMillis();
        System.out.println("\tprocessing .que file took " + (queEnd - queStart) + "ms");

        System.out.println("Computing one-to-all Dijkstra from node id " + sourceNodeId);
        long oneToAllStart = System.currentTimeMillis();
        int[] distancesFromStart = g.oneToAllDijkstra(sourceNodeId);
        long oneToAllEnd = System.currentTimeMillis();
        System.out.println("\tone-to-all Dijkstra took " + (oneToAllEnd - oneToAllStart) + "ms");

        // ask user for a target node id
        System.out.print("Enter target node id... ");
        Scanner scanner = new Scanner(System.in);
        int targetNodeId = (scanner).nextInt();
        scanner.close();
        int oneToAllDistance = -42;
        oneToAllDistance = distancesFromStart[targetNodeId];
        System.out.println("Distance from " + sourceNodeId + " to " + targetNodeId + " is " + oneToAllDistance);
    }

}
