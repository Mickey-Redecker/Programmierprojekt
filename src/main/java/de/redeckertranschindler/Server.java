package de.redeckertranschindler;

import static de.redeckertranschindler.Graph.X;
import static de.redeckertranschindler.Graph.Y;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import de.redeckertranschindler.util.DijkstraResult;
import de.redeckertranschindler.util.Point;

public class Server {

    private final Graph g;
    private final QuadTree tree;

    DijkstraResult dijkstraResult;

    public Server(final String graphPath, final int port) throws IOException {

        g = new Graph(graphPath);
        tree = new QuadTree(g);

        final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);

        final HttpContext indexContext = server.createContext("/index");
        indexContext.setHandler(this::handlePages);
        final HttpContext leafletContext = server.createContext("/leaflet");
        leafletContext.setHandler(this::handlePages);

        final HttpContext dijkstraContext = server.createContext("/dijkstra");
        dijkstraContext.setHandler(this::handleDijkstraRequest);

        final HttpContext pathContext = server.createContext("/path");
        pathContext.setHandler(this::handlePathRequest);

        final HttpContext closestContext = server.createContext("/closest");
        closestContext.setHandler(this::handleClosestRequest);

        server.start();
        System.out.println("server started on: http://localhost:" + port + "/index.html");
    }

    private void handleClosestRequest(final HttpExchange exchange) throws IOException {
        final URI requestURI = exchange.getRequestURI();
        final String query = requestURI.getQuery();

        final String[] parts = query.split("&");

        double lat = Double.NaN;
        double lon = Double.NaN;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("lat=")) {
                lat = Double.valueOf(parts[i].substring(4));
            } else if (parts[i].startsWith("lon=")) {
                lon = Double.valueOf(parts[i].substring(4));
            }
        }

        if (Double.isNaN(lat) || Double.isNaN(lon)) {
            final String errorResponse = "Wrong Requestformat!";
            exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
            return;
        }

        final int node = tree.getClosestNode(new Point(lat, lon));
        final List<Double> coordinateList = new ArrayList<>();
        final double[][] coordinates = g.getNodes();

        coordinateList.add(coordinates[Y][node]);
        coordinateList.add(coordinates[X][node]);

        final String response = Integer.toString(node) + """
                { \"type\": \"Point\",
                    \"coordinates\":
                """ + coordinateList.toString() + """
                    }
                """;

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleDijkstraRequest(final HttpExchange exchange) throws IOException {
        final URI requestURI = exchange.getRequestURI();
        final String query = requestURI.getQuery();

        final String[] parts = query.split("&");

        int src = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("src=")) {
                src = Integer.valueOf(parts[i].substring(4));
            }
        }

        if (src < 0) {
            final String errorResponse = "Wrong Requestformat!";
            exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
            return;
        }

        dijkstraResult = g.dijkstra(src);

        final String response = "";

        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePathRequest(final HttpExchange exchange) throws IOException {
        final URI requestURI = exchange.getRequestURI();
        final String query = requestURI.getQuery();

        final String[] parts = query.split("&");

        int src = -1;
        int target = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].startsWith("src=")) {
                src = Integer.valueOf(parts[i].substring(4));
            } else if (parts[i].startsWith("target=")) {
                target = Integer.valueOf(parts[i].substring(7));
            }
        }

        if (src < 0 || target < 0) {
            final String errorResponse = "Wrong Requestformat!";
            exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
            return;
        }

        final List<List<Double>> resList = new ArrayList<>();

        final double[][] coordinates = g.getNodes();
        if (!(dijkstraResult.src == src)) {
            final String errorResponse = "Wrong startnode!";
            exchange.sendResponseHeaders(200, errorResponse.getBytes().length);
            final OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
            return;
        }

        int currentNode = target;

        while (currentNode != src) {
            final List<Double> point = new ArrayList<>();
            point.add(coordinates[Y][currentNode]);
            point.add(coordinates[X][currentNode]);
            resList.add(point);
            currentNode = dijkstraResult.previousNodes[currentNode];
        }

        final List<Double> point = new ArrayList<>();
        point.add(coordinates[Y][src]);
        point.add(coordinates[X][src]);
        resList.add(point);

        final String response = "{\"type\": \"LineString\", \"coordinates\": " + resList.toString() + "}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void handlePages(HttpExchange t) throws IOException {
        String root = "src/main/resources";
        URI uri = t.getRequestURI();
        File file = new File(root + uri.getPath()).getCanonicalFile();
        if (!file.isFile()) {
            String response = "404 (Not Found)\n";
            t.sendResponseHeaders(404, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }
            fs.close();
            os.close();
        }
    }
}
