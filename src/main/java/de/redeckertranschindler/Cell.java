package de.redeckertranschindler;

public class Cell {
    private final int x;
    private final int y;

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getCellX() {
        return this.x;
    }

    public int getCellY() {
        return this.y;
    }
}
