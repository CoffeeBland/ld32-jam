package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Created by dagothig on 4/26/15.
 */
public class Collision {
    protected static final Stack<Collision> pool = new Stack<>();

    public Actor colliding;
    public Entity collided;
    public float timeToCollision;
    /**
     * Before timeToCollision is applied
     */
    public Vector3 collidingVel;
    public Vector3 collidingPoint;
    public Type type;

    protected Collision() {

    }

    public Collision reset() {
        colliding = null;
        collided = null;
        timeToCollision = 0;
        collidingVel = null;
        collidingPoint = null;
        type = null;
        return this;
    }

    @NotNull
    public static Collision col() {
        if (pool.isEmpty()) return new Collision();
        else return pool.pop();
    }
    public static void claim(@NotNull Collision col) {
        pool.push(col.reset());
    }

    public enum Type {
        SIDE, VERTICAL, BOTH
    }
}
