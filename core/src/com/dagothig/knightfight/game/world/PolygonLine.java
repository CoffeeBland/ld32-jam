package com.dagothig.knightfight.game.world;

import com.badlogic.gdx.math.Vector2;
import com.dagothig.knightfight.util.VectorPool;

/**
 * Created by dagothig on 4/19/15.
 */
public class PolygonLine {
    public final Vector2 p1, p2, direction, normal;

    public PolygonLine(Vector2 p1, Vector2 p2) {
        this.p1 = p1;
        this.p2 = p2;
        direction = VectorPool.V2().set(p2).sub(p1);
        normal = direction.cpy().nor();
        float temp = normal.x;
        normal.x = -normal.y;
        normal.y = temp;
    }
}
