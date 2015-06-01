package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.util.VectorPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Actor extends HeightEntity {
    public static final float MIN_DISTANCE = 0.001f;

    public final Vector3 velocity = new Vector3();
    public boolean
            canCollide = true,
            affectedByGravity = true,
            affectedByFriction = true;

    public Actor(boolean canCollide, boolean canBeCollided,
                 float mass, float height,
                 boolean affectedByGravity, boolean affectedByFriction) {
        super(canBeCollided, mass, height);
        this.canCollide = canCollide;
        this.affectedByGravity = affectedByGravity;
        this.affectedByFriction = affectedByFriction;
    }

    @Nullable
    public abstract Collision getSideCollision(@NotNull Entity entity, @NotNull Vector3 vel);
    @Nullable
    public Collision getVerticalCollision(@NotNull Entity entity, @NotNull Vector3 vel) {
        if (!(entity instanceof HeightEntity)) return null;
        HeightEntity hEntity = (HeightEntity)entity;
        if (vel.z == 0) return null;
        float timeToUnder = (entity.pos.z - (pos.z + height)) / vel.z;
        boolean timeToUnderValid = timeToUnder > -MIN_DISTANCE && timeToUnder < 1;
        float timeToTop = ((entity.pos.z + hEntity.height) - pos.z) / vel.z;
        boolean timeToTopValid = timeToTop > -MIN_DISTANCE && timeToTop < 1;

        if (!timeToUnderValid && !timeToTopValid) return null;

        Collision col = Collision.col();
        col.timeToCollision =
                (!timeToUnderValid ? timeToTop :
                (!timeToTopValid ? timeToUnder :
                (Math.min(timeToUnder, timeToTop))));
    }

    @Nullable
    public Collision getCollision(@NotNull Entity entity, @NotNull Vector3 vel) {
        Collision sideCol = getSideCollision(entity, vel);
        Collision verticalCol = getVerticalCollision(entity, vel);
        if (sideCol == null && verticalCol == null) return null;
        if (sideCol == null) return verticalCol;
        if (verticalCol == null) return sideCol;
        if (sideCol.timeToCollision < verticalCol.timeToCollision) {
            Collision.claim(verticalCol);
            return sideCol;
        }
        if (verticalCol.timeToCollision < sideCol.timeToCollision) {
            Collision.claim(sideCol);
            return verticalCol;
        }

        Collision col = Collision.col();
        col.collidingVel = vel;
        col.colliding = this;
        col.collided = entity;
        col.type = Collision.Type.BOTH;
        col.timeToCollision = verticalCol.timeToCollision;

        Collision.claim(verticalCol);
        Collision.claim(sideCol);

        return col;
    }
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
        if (affectedByFriction) velocity.scl(world.airFriction);

        Vector3 movement = VectorPool.V3().set(velocity);

        positionCheck: {
            // Abort movement for very small deltas
            if (movement.len2() < MIN_DISTANCE) break positionCheck;

            Collision col = resolveShortestCollision(delta, world, movement);
            if (col != null) {
                col.colliding.reactToCollision(col);
                col.collided.reactToCollision(col);
                pos.add(col.collidingVel);
                Collision.claim(col);
            } else {
                pos.add(movement);
            }
        }

        VectorPool.claim(movement);
    }
}
