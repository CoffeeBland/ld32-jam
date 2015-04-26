package com.dagothig.knightfight.game.world;

public class Polygon {
    protected boolean deadly;
    protected boolean exclusive;
    protected int[][] points;

    public Polygon(int[][] points, boolean deadly, boolean exclusive) {
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
