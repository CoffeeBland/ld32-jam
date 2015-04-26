package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.util.VectorPool;

/**
 * Created by dagothig on 4/26/15.
 */
public abstract class Actor extends Entity {
    public static final float MIN_DISTANCE = 0.001f;

    public final Vector3 velocity = new Vector3();

    public abstract void reactToCollision(Collision collision);

    public abstract void resolveShortestCollision(Collision selfCol, Collision outCol, float delta, World world);
    @Override
    public void update(float delta, World world) {
        velocity.add(world.gravity);

        velocity.x *= world.airFriction;
        velocity.y *= world.airFriction;
        velocity.z *= world.airFriction;

        Vector3 movement = VectorPool.V3();

        // Abort movement for very small deltas
        positionCheck: {
            if (Math.abs(movement.x) < MIN_DISTANCE
                    && Math.abs(movement.y) < MIN_DISTANCE
                    && Math.abs(movement.z) < MIN_DISTANCE) {
                break positionCheck;
            }

            Collision selfCol = Collision.col();
            Collision outCol = Collision.col();
            resolveShortestCollision(selfCol, outCol, delta, world);
            if (selfCol.actor != null) {
                reactToCollision(selfCol);
                selfCol.actor.reactToCollision(outCol);
            }

            Collision.claim(selfCol);
            Collision.claim(outCol);
        }

        VectorPool.claim(movement);
    }

}
