package de.redeckertranschindler.util;

public class DijkstraResult {

    public final int[] distance;
    public final int[] previousNodes;
    public final int target;

    public DijkstraResult(final int[] distance, final int[] previousNodes, final int target) {
        this.distance = distance;
        this.previousNodes = previousNodes;
        this.target = target;
    }

    public DijkstraResult(final int[] distance, final int[] previousNodes) {
        this.distance = distance;
        this.previousNodes = previousNodes;
        this.target = -1;
    }
}
