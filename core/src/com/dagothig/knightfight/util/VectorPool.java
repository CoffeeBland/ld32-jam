package com.dagothig.knightfight.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Created by dagothig on 4/18/15.
 */
public class VectorPool {
    protected static final Stack<Vector3> pool3 = new Stack<>();
    protected static final Stack<Vector2> pool2 = new Stack<>();

    @NotNull
    public static Vector3 V3() {
        if (pool3.isEmpty()) return new Vector3();
        else return pool3.pop().setZero();
    }
    public static void claim(@NotNull Vector3 vec) {
        pool3.push(vec);
    }

    @NotNull
    public static Vector2 V2() {
        if (pool2.isEmpty()) return new Vector2();
        else return pool2.pop().setZero();
    }
    public static void claim(@NotNull Vector2 vec) {
        pool2.push(vec);
    }
}
