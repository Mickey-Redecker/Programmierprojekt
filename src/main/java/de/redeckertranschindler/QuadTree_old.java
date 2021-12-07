// package de.redeckertranschindler;

// import java.util.ArrayList;
// import java.util.List;

// import static de.redeckertranschindler.Graph.X;
// import static de.redeckertranschindler.Graph.Y;

// public class QuadTree {

// private static final int CAPACITY = 500;

// private final double[][] coordinates;

// private Rectangle boundary;
// private ArrayList<Integer> points;

// private QuadTree northWest;
// private QuadTree northEast;
// private QuadTree southWest;
// private QuadTree southEast;

// public QuadTree(final Graph g) {
// this(g.getBoundary(), g.getNodes());
// }

// public QuadTree(final Rectangle boundary, final double[][] nodes) {
// this.boundary = boundary;
// points = new ArrayList<>(CAPACITY);
// northWest = null;
// northEast = null;
// southWest = null;
// southEast = null;
// this.coordinates = nodes;

// }

// public boolean insert(final Point p, final int id) {
// if (!boundary.containsPoint(p)) {
// // System.err.println("Point out of boundary");
// return false;
// }

// if (points.size() < CAPACITY && northWest == null) {
// // System.out.println("p add" + id);
// points.add(id);
// return true;
// }

// if (northWest == null)
// subdivide();

// return northWest.insert(p, id) ||
// northEast.insert(p, id) ||
// southWest.insert(p, id) ||
// southEast.insert(p, id);

// }

// public void subdivide() {
// double dimension = this.boundary.getHalfDimension() / 2;
// double centerX = this.boundary.getCenter().getX();
// double centerY = this.boundary.getCenter().getY();

// northWest = new QuadTree(new Rectangle(new Point(centerX - dimension, centerY
// + dimension), dimension),
// coordinates);
// northEast = new QuadTree(new Rectangle(new Point(centerX + dimension, centerY
// + dimension), dimension),
// coordinates);
// southWest = new QuadTree(new Rectangle(new Point(centerX - dimension, centerY
// - dimension), dimension),
// coordinates);
// southEast = new QuadTree(new Rectangle(new Point(centerX + dimension, centerY
// - dimension), dimension),
// coordinates);
// }

// private double distance(final int node1, final int node2) {
// return distance(coordinates[X][node1], coordinates[Y][node1],
// coordinates[X][node2], coordinates[Y][node2]);
// }

// private double distance(final double x1, final double y1, final int node2) {
// return distance(x1, y1, coordinates[X][node2], coordinates[Y][node2]);
// }

// private double distance(final double x1, final double y1, final Point p2) {
// return distance(x1, y1, p2.getX(), p2.getY());
// }

// private double distance(final Point p1, final int node2) {
// return distance(p1.getX(), p1.getY(), coordinates[X][node2],
// coordinates[Y][node2]);
// }

// private double distance(final Point p1, final Point p2) {
// return distance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
// }

// private double distance(final double x1, final double y1, final double x2,
// final double y2) {
// return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
// }

// public int getClosestNode(final Point p) {
// double s = 0.1;
// Rectangle range = new Rectangle(p, s);

// int nodeId = -1;
// double minDistance = Double.MAX_VALUE;
// int n = -1;

// while (nodeId < 0) {

// ArrayList<Integer> result = new ArrayList<>();
// queryRange(range, result);
// System.out.println("Amount of points in range: " + result.size());
// for (Integer i : result) {
// double currentDistance = distance(p, i);
// if (minDistance > currentDistance) {
// n = i;
// minDistance = currentDistance;
// }
// }

// if (!(n == -1) && distance(p, n) < range.getHalfDimension()) {
// return n;
// } else {
// range = new Rectangle(p, range.getHalfDimension() + s);
// }
// }

// return nodeId;
// }

// public void queryRange(final Rectangle range, List<Integer> results) {
// if (!boundary.intersects(range)) {
// // System.out.println("no inters");
// return;
// }

// for (int i = 0; i < points.size(); i++) {
// if (points.get(i) != null) {
// // System.out.println("p" + i);
// double x = coordinates[X][i];
// double y = coordinates[Y][i];

// if (range.containsPoint(new Point(x, y))) {
// results.add(points.get(i));
// }
// }
// }

// if (northWest == null) {
// return;
// }

// northWest.queryRange(range, results);
// northEast.queryRange(range, results);
// southEast.queryRange(range, results);
// southWest.queryRange(range, results);
// }

// // public int getClosest(final Point p) {

// // if (points.size() == 0) {
// // return -1;
// // }

// // double distance = Double.MAX_VALUE;
// // int id = -1;

// // for (int i = 0; i < points.size(); i++) {
// // double x = coordinates[X][i];
// // double y = coordinates[Y][i];
// // double currentDistance = distance(x, y, p);

// // if (distance > currentDistance) {
// // distance = currentDistance;
// // id = i;
// // }
// // }

// // if (northWest == null) {
// // return id;
// // }

// // ArrayList<Integer> results = new ArrayList<>();
// // Rectangle range = new Rectangle(p, distance);

// // northWest.queryRange(range, results);
// // northEast.queryRange(range, results);
// // southEast.queryRange(range, results);
// // southWest.queryRange(range, results);

// // return getClosest(p, results);
// // }

// // public int getClosestNode(final Point p) {
// // double s = 0.05;
// // Rectangle range = new Rectangle(p, s);

// // int nodeId = -1;
// // double minDistance = Double.MAX_VALUE;
// // int n = -1;

// // while (nodeId < 0) {

// // ArrayList<Integer> result = new ArrayList<>();
// // boolean res = queryRange(range, result);

// // System.out.println("Amount of points in range: " + result.size());

// // if (res) {
// // double minDist = Double.MAX_VALUE;
// // for (int i = 0; i < result.size(); i++) {
// // double currDist = distance(p, i);
// // if (minDist > currDist) {
// // minDist = currDist;
// // }
// // }

// // range = new Rectangle(p, minDist);
// // continue;
// // }

// // for (Integer i : result) {
// // double currentDistance = distance(p, i);
// // if (minDistance > currentDistance) {
// // n = i;
// // minDistance = currentDistance;
// // }
// // }

// // if (!(n == -1) && distance(p, n) < range.getHalfDimension()) {
// // return n;
// // } else {
// // range = new Rectangle(p, range.getHalfDimension() + s);
// // }
// // }

// // return nodeId;
// // }

// // public boolean queryRange(final Rectangle range, List<Integer> results) {
// // if (!boundary.intersects(range)) {
// // // System.out.println("no inters");
// // return false;
// // }

// // if (results.size() > 1000) {
// // return true;
// // }

// // for (int i = 0; i < points.size(); i++) {
// // if (points.get(i) != null) {
// // // System.out.println("p" + i);
// // double x = coordinates[X][i];
// // double y = coordinates[Y][i];

// // if (range.containsPoint(new Point(x, y))) {
// // results.add(points.get(i));
// // }
// // }
// // }

// // if (northWest == null) {
// // return false;
// // }

// // return northWest.queryRange(range, results) ||
// // northEast.queryRange(range, results) ||
// // southEast.queryRange(range, results) ||
// // southWest.queryRange(range, results);
// // }

// }
