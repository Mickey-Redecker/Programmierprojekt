package de.redeckertranschindler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import de.redeckertranschindler.util.Point;

public class Benchmark {

    public static void main(final String[] args) {
        // read parameters (parameters are expected in exactly this order)
        final String graphPath = args[1];
        final double lon = Double.parseDouble(args[3]);
        final double lat = Double.parseDouble(args[5]);
        final String quePath = args[7];
        final int sourceNodeId = Integer.parseInt(args[9]);

        // run benchmarks
        System.out.println("Reading graph file " + graphPath);
        final long graphReadStart = System.currentTimeMillis();
        Graph g = null;
        try {
            g = new Graph(graphPath);
        } catch (final IOException e1) {
            System.out.println("Couldn't find the given graph-file!!!");
            System.exit(1);
        }
        final long graphReadEnd = System.currentTimeMillis();
        System.out.println("\tgraph read took " + (graphReadEnd - graphReadStart) + "ms");

        System.out.println("Generating quadtree, n = " + g.getNodeAmount());
        System.out.println("\ttime is not critical!");
        final long generateTreeStart = System.currentTimeMillis();
        final QuadTree tree = new QuadTree(g);
        final long generateTreeEnd = System.currentTimeMillis();
        System.out.println("\tgenerating quadtree took " + (generateTreeEnd - generateTreeStart) + "ms");

        System.out.println("Finding closest node to coordinates " + lon + " " + lat);
        final long nodeFindStart = System.currentTimeMillis();
        final int nodeId = tree.getClosestNode(new Point(lat, lon));
        final long nodeFindEnd = System.currentTimeMillis();
        System.out.println("\tfound node: " + nodeId);
        System.out.println("\tfinding node took " + (nodeFindEnd - nodeFindStart) + "ms");

        System.out.println("Running one-to-one Dijkstras for queries in .que file " + quePath);
        final long queStart = System.currentTimeMillis();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(quePath))) {
            String currLine;
            while ((currLine = bufferedReader.readLine()) != null) {
                final int oneToOneSourceNodeId = Integer.parseInt(currLine.substring(0, currLine.indexOf(" ")));
                final int oneToOneTargetNodeId = Integer.parseInt(currLine.substring(currLine.indexOf(" ") + 1));
                final int oneToOneDistance = g.oneToOneDijkstra(oneToOneSourceNodeId, oneToOneTargetNodeId);
                System.out.println(oneToOneDistance);
            }
        } catch (final IOException e) {
            System.out.println("Couldn't find the given que-file!!!");
            System.exit(1);
        }
        final long queEnd = System.currentTimeMillis();
        System.out.println("\tprocessing .que file took " + (queEnd - queStart) + "ms");

        System.out.println("Computing one-to-all Dijkstra from node id " + sourceNodeId);
        final long oneToAllStart = System.currentTimeMillis();
        final int[] distancesFromStart = g.oneToAllDijkstra(sourceNodeId);
        final long oneToAllEnd = System.currentTimeMillis();
        System.out.println("\tone-to-all Dijkstra took " + (oneToAllEnd - oneToAllStart) + "ms");

        // ask user for a target node id
        System.out.print("Enter target node id... ");
        final Scanner scanner = new Scanner(System.in);
        final int targetNodeId = scanner.nextInt();
        scanner.close();
        final int oneToAllDistance = distancesFromStart[targetNodeId];
        System.out.println("Distance from " + sourceNodeId + " to " + targetNodeId + " is " + oneToAllDistance);
    }

}
