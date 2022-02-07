package de.redeckertranschindler.util;

public class DijkstraResult {

    public final int[] distance;
    public final int[] previousNodes;
    public final int src;

    public DijkstraResult(final int[] distance, final int[] previousNodes, final int src) {
        this.distance = distance;
        this.previousNodes = previousNodes;
        this.src = src;
    }

    // public DijkstraResult(final int[] distance, final int[] previousNodes) {
    // this.distance = distance;
    // this.previousNodes = previousNodes;
    // }
}
