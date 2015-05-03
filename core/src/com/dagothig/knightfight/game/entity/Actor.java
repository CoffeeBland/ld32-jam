package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.util.VectorPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Actor extends Entity {
    public static final float MIN_DISTANCE = 0.001f;

    public final Vector3 velocity = new Vector3();
    public boolean
            affectedByGravity = true,
            affectedByFriction = true;

    public Actor(boolean canBeCollided, float mass, boolean affectedByGravity, boolean affectedByFriction) {
        super(canBeCollided, mass);
        this.affectedByGravity = affectedByGravity;
        this.affectedByFriction = affectedByFriction;
    }

    public abstract void reactToCollision(@NotNull Collision collision);
    @Nullable
    public abstract Collision getCollision(@NotNull Entity entity, @NotNull Vector3 vel);
    @Nullable
    public Collision resolveShortestCollision(float delta, @NotNull World world, @NotNull Vector3 vel) {
        Collision shortestCollision = null;
        for (Entity entity : world.entitiesLayer.entities) {
            if (entity == this || !entity.canBeCollided) continue;
            Collision col = getCollision(entity, vel);
            if (col == null) continue;
            if (shortestCollision == null || col.timeToCollision < shortestCollision.timeToCollision) {
                if (shortestCollision != null) Collision.claim(shortestCollision);
                shortestCollision = col;
            }
        }
        return shortestCollision;
    }
    @Override
    public void update(float delta, @NotNull World world) {
        if (affectedByGravity) velocity.add(world.gravity);

        if (affectedByFriction) {
            velocity.x *= world.airFriction;
            velocity.y *= world.airFriction;
            velocity.z *= world.airFriction;
        }

        Vector3 movement = VectorPool.V3();

        // Abort movement for very small deltas
        positionCheck: {
            if (movement.len2() < MIN_DISTANCE) break positionCheck;

            Collision col = resolveShortestCollision(delta, world, movement);
            if (col != null) {
                col.colliding.reactToCollision(col);
                col.collided.reactToCollision(col);
                Collision.claim(col);
            }
        }

        VectorPool.claim(movement);
    }
}
