package com.dagothig.knightfight.game.entity;

import java.util.Stack;

/**
 * Created by dagothig on 4/26/15.
 */
public class Collision {
    protected static final Stack<Collision> pool = new Stack<>();

    public Actor actor;
    public float timeToCollision, angleOfCollision;

    public Collision reset() {
        actor = null;
        timeToCollision = 0;
        angleOfCollision = 0;
        return this;
    }

    public static Collision col() {
        if (pool.isEmpty()) return new Collision();
        else return pool.pop();
    }
    public static void claim(Collision col) {
        pool.push(col.reset());
    }
}
