package com.dagothig.knightfight.game;

public class WorldPolygon {
    protected boolean deadly;
    protected boolean exclusive;
    protected int[][] points;

    public WorldPolygon(int[][] points, boolean deadly, boolean exclusive) {
        this.points = points;
        this.deadly = deadly;
        this.exclusive = exclusive;
    }

    public boolean isDeadly() { return deadly; }
    public boolean isExclusive() { return exclusive; }

    public boolean contains(int x, int y) {
        return false;
    }
}
