package com.dagothig.knightfight.state;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagothig on 4/18/15.
 */
public class VectorPool {
    protected static final List<Vector3> pool = new ArrayList<>();

    public static Vector3 V3() {
        if (pool.size() <= 0) return new Vector3();
        else return pool.remove(pool.size() - 1).setZero();
    }
    public static void reclaim(Vector3 vec) {
        pool.add(vec);
    }
}
