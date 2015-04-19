package com.dagothig.knightfight.state;

import com.badlogic.gdx.math.Vector3;

import java.util.Stack;

/**
 * Created by dagothig on 4/18/15.
 */
public class VectorPool {
    protected static final Stack<Vector3> pool = new Stack<>();

    public static Vector3 V3() {
        if (pool.isEmpty()) return new Vector3();
        else return pool.pop().setZero();
    }
    public static void claim(Vector3 vec) {
        pool.push(vec);
    }
}
