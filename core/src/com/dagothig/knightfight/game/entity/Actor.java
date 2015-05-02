package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.util.VectorPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by dagothig on 4/26/15.
 */
public abstract class Actor extends Entity {
    public static final float MIN_DISTANCE = 0.001f;

    public final Vector3 velocity = new Vector3();

    public abstract void reactToCollision(@NotNull Collision collision);
    @Nullable
    public abstract Collision resolveShortestCollision(float delta, @NotNull World world);
    @Override
    public void update(float delta, @NotNull World world) {
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

            Collision col = resolveShortestCollision(delta, world);
            resolveShortestCollision(delta, world).colliding.update(delta, world);
            if (col != null) {
                col.colliding.reactToCollision(col);
                col.collided.reactToCollision(col);
                Collision.claim(col);
            }
        }

        VectorPool.claim(movement);
    }
}
