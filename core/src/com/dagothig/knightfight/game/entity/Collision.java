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
    public Actor collided;
    public float timeToCollision;
    public float angleOfCollision;
    public Vector3 collidingVel;
    public Type type;

    public Collision clampAngle() {
        while (angleOfCollision < 0) angleOfCollision += Math.PI * 2;
        angleOfCollision %= Math.PI * 2;
        return this;
    }

    public Collision reverseAngle() {
        angleOfCollision = (float)((angleOfCollision + Math.PI) % (Math.PI * 2));
        return this;
    }

    public Collision reset() {
        colliding = null;
        collided = null;
        timeToCollision = 0;
        angleOfCollision = 0;
        collidingVel = null;
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
        SIDE, VERTICAL
    }
}
