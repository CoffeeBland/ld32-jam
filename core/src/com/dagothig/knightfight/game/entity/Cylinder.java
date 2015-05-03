package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.Person;
import com.dagothig.knightfight.game.world.PolygonLine;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.util.VectorPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by dagothig on 4/26/15.
 */
public abstract class Cylinder extends Actor {
    public static final float
            INERTIA = 0.1f;

    public float radius, radiusSquared, height;


    public Cylinder(boolean canBeCollided, float mass,
                    boolean affectedByGravity, boolean affectedByFriction,
                    float radius, float height) {
        super(canBeCollided, mass, affectedByGravity, affectedByFriction);
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.height = height;
    }

    // === Cylinder collision calculations === //
    public float distanceSquared(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        return Vector2.dst2(pos.x + vel.x, pos.y + vel.y, cylinder.pos.x, cylinder.pos.y);
    }
    @Nullable
    public Float timeToSideCollision(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        /*
        On pose r3 = r1² + r2²
        xd = x1 - x2
        yd = y1 - y2
        On cherche le moment où la distance entre les deux cercles est égale à 0,
        ou plutôt: d(r1, r2) = d((x1, y1) + (vx, vt)t - (x2, y2)), résolut pour t
        => r1² + r2² = (x1 + vx*t - x2)² + (y1 + vy*t - y2)²
        <=> r3 = (xd + vx*t)² + (yd + vy*t)²
        (où toutes les variables \ {t} sont des constantes)
        En factorisant, on arrive à la forme
        0 = (vx² + vy²)t² + (2*xd*vx + 2*yd*vy)t + xd² + yd² - r3
        Étant donné l'équation quadratique
        0 = ax² + bx + c
        on a que
        t = (-b +/-racine(b² - 4ac)) / 2a
        => a = vx² + vy², b = 2*xd*vx + 2*yd*vy, c = xd² + yd² - r3
        */
        float r3 = radiusSquared + cylinder.radiusSquared;
        float xd = pos.x - cylinder.pos.x;
        float yd = pos.y - cylinder.pos.y;
        float a = vel.x * vel.x + vel.y * vel.y;
        float b = 2 * xd * vel.x + 2 * yd * vel.y;
        float c = xd * xd + yd * yd - r3;
        if (a == 0) return null;
        float discriminantSquared = b * b - 4 * a * c;
        if (discriminantSquared < 0) return null;
        float discriminant = (float)Math.sqrt(discriminantSquared);
        float t1 = (-b + discriminant) / (2 * a);
        float t2 = (-b - discriminant) / (2 * a);
        return (t1 < -MIN_DISTANCE && t2 < -MIN_DISTANCE ? null :
                (t1 < 0 ? t2 :
                (t2 < 0 ? t1 :
                Math.min(t1, t2))));
    }
    @Nullable
    public Float timeToVerticalCollision(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        if (vel.z == 0) return null;
        float timeToUnder = (pos.z + height - cylinder.pos.z) / vel.z;
        float timeToTop = (pos.z + height - cylinder.pos.z) / vel.z;
        return (timeToUnder < -MIN_DISTANCE && timeToTop < -MIN_DISTANCE ? null :
                (timeToUnder < 0 ? timeToTop:
                (timeToTop < 0 ? timeToUnder:
                Math.min(timeToUnder, timeToTop))));
    }
    public static float angleOf(Vector3 vec) {
        return (float)Math.atan2(vec.y, vec.x);
    }
    public float angleAtCollision(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        Vector3 velPos = VectorPool.V3();
        velPos.set(pos).add(vel).sub(cylinder.pos);
        float angle = angleOf(velPos);
        VectorPool.claim(velPos);
        return angle;
    }

    public boolean isWithinDistance(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        return distanceSquared(cylinder, vel) - (radiusSquared + cylinder.radiusSquared) < MIN_DISTANCE;
    }
    public boolean isWithinDistanceAtPos(@NotNull Cylinder cylinder, @NotNull Vector3 pos) {
        return Vector2.dst2(pos.x, pos.y, cylinder.pos.x, cylinder.pos.y) - (radiusSquared + cylinder.radiusSquared) < MIN_DISTANCE;
    }
    public boolean isWithinHeight(@NotNull Cylinder cylinder) {
        return pos.z - (cylinder.pos.z + cylinder.height) < MIN_DISTANCE && pos.z + height - pos.z > -MIN_DISTANCE;
    }
    public boolean isWithinHeightAtPos(@NotNull Cylinder cylinder, @NotNull Vector3 pos) {
        return pos.z - (cylinder.pos.z + cylinder.height) < MIN_DISTANCE && (pos.z + height) - cylinder.pos.z > -MIN_DISTANCE;
    }
    public boolean goingIntoFromAbove(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        return vel.z < MIN_DISTANCE
                && pos.z - (cylinder.pos.z + cylinder.height) > -MIN_DISTANCE
                && pos.z + vel.z - (cylinder.pos.z + cylinder.height) < MIN_DISTANCE;
    }
    public boolean goingIntoFromUnder(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        return vel.z > -MIN_DISTANCE
                && (pos.z + height) - cylinder.pos.z < MIN_DISTANCE
                && (pos.z + height) + vel.z - cylinder.pos.z > -MIN_DISTANCE;
    }

    public boolean isInCollision(@NotNull World world, @NotNull Vector3 vel) {
        Vector3 newPos = VectorPool.V3().set(pos).add(vel);

        boolean hadCol = false;
        for (Entity entity : world.entitiesLayer.entities) {
            if (!(entity instanceof Cylinder) || entity == this) continue;
            Cylinder cylinder = (Cylinder) entity;

            if (isWithinDistanceAtPos(cylinder, newPos) && isWithinHeightAtPos(cylinder, newPos)) {
                hadCol = true;
                break;
            }
        }

        VectorPool.claim(newPos);
        return hadCol;
    }

    public void reactToCollision(Person person, Vector3 personVel, float colAngle) {
        float movAngle = angleOf(velocity);
        float newAngle = 2 * colAngle - movAngle;
        float velN = (float)Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
        velocity.x = ((velN * -(float)Math.cos(newAngle)) + personVel.x) * 0.5f;
        velocity.y = ((velN * -(float)Math.sin(newAngle)) + personVel.y) * 0.5f;
    }
    public void reactToCollision(Person person, Vector3 personVel) {
        velocity.z *= -1f;
    }

    // === Polygon collision calculations === //
    public Vector2 pointOnRim(PolygonLine line, Vector3 vec) {
        Vector2 vecTo = VectorPool.V2().set(line.p2.x - pos.x, line.p2.y - pos.y);
        Vector2 projection = VectorPool.V2().set(line.direction).scl(vecTo.dot(line.direction) / line.direction.dot(line.direction));
        Vector2 orthogonal = VectorPool.V2().set(vecTo).sub(projection);
        Vector2 pt = VectorPool.V2().set(orthogonal).scl(radius / orthogonal.len()).add(pos.x, pos.y);
        VectorPool.claim(vecTo);
        VectorPool.claim(projection);
        VectorPool.claim(orthogonal);
        return pt;
    }
    public Float timeToSideCollision(PolygonLine line, Vector3 vec) {
        Vector2 refPt = pointOnRim(line, vec);

        VectorPool.claim(refPt);
        return null;
    }

    public void updatePos(float delta, World world) {
        // Velocities are applied differently when on the ground or not
        if (pos.z < MIN_DISTANCE) {
            velocity.x *= world.groundFriction;
            velocity.y *= world.groundFriction;
        }
        else {
            velocity.x *= world.airFriction;
            velocity.y *= world.airFriction;
        }
        velocity.z = (velocity.z - world.gravity) * world.airFriction;

        // Cap to ground
        // TODO: replace with polygon check
        if (pos.z + velocity.z < MIN_DISTANCE) velocity.z = -pos.z;


        Vector3 mov = VectorPool.V3().set(velocity);
        // Inertia
        if (mov.x < INERTIA && mov.x > -INERTIA) mov.x = 0;
        if (mov.y < INERTIA && mov.y > -INERTIA) mov.y = 0;
        if (mov.z < INERTIA && mov.z > -INERTIA) mov.z = 0;

        // Don't check for very small deltas (just not worth it)
        if (Math.abs(mov.x) < MIN_DISTANCE && Math.abs(mov.y) < MIN_DISTANCE && Math.abs(mov.z) < MIN_DISTANCE) return;

        for (Entity entity : world.entitiesLayer.entities) {
            // We only check collisions with other people (this is high society after all!)
            if (!(entity instanceof Person) || entity == this) continue;
            Person person = (Person) entity;

            if (isWithinDistance(person, mov)) {
                if (isWithinHeight(person)) {
                    Float t = timeToCollision(person, mov);
                    if (t == null || t >= 1 || t <= -1) continue;
                    mov.x *= t;
                    mov.y *= t;
                    float movAngle = angleOf(mov);
                    float colAngle = angleAtCollision(person, mov);
                    float newAngle = 2 * colAngle - movAngle;
                    float velN = Vector2.len(velocity.x, velocity.y);
                    Vector3 tmpVel = VectorPool.V3().set(velocity);
                    velocity.x = ((velN * -(float)Math.cos(newAngle)) + person.velocity.x) * 0.5f;
                    velocity.y = ((velN * -(float)Math.sin(newAngle)) + person.velocity.y) * 0.5f;
                    person.reactToCollision(this, tmpVel, colAngle + (float)Math.PI);
                    VectorPool.claim(tmpVel);
                }
                if (goingIntoFromAbove(person, mov)) {
                    // We are above
                    person.reactToCollision(this, velocity);
                    reactToCollision(person, person.velocity);
                    mov.z = 0;
                } else if (goingIntoFromUnder(person, mov)) {
                    // We are below
                    person.reactToCollision(this, velocity);
                    reactToCollision(person, person.velocity);
                    mov.z = 0;
                }
            }
        }
        pos.add(mov);
        VectorPool.claim(mov);
    }

    @Override
    public void reactToCollision(@NotNull Collision col) {
        switch (col.type) {
            case SIDE:

                float movAngle = angleOf(velocity);
                float newAngle = 2 * col.angleOfCollision - movAngle;
                float velN = (float)Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
                velocity.x = ((velN * -(float)Math.cos(newAngle)) + personVel.x) * 0.5f;
                velocity.y = ((velN * -(float)Math.sin(newAngle)) + personVel.y) * 0.5f;
                break;
            case VERTICAL:
                break;
        }
    }

    @Override
    public void update(float delta, @NotNull World world) {
        updatePos(delta, world);
    }

    @NotNull
    public Collision getSideCollision(@NotNull Collision col, @NotNull Cylinder cylinder, @NotNull Vector3 vel, float timeToCol) {
        col.timeToCollision = timeToCol;
        col.angleOfCollision = angleAtCollision(cylinder, vel);
        col.type = Collision.Type.SIDE;

        return col;
    }
    @NotNull
    public Collision getVerticalCollision(@NotNull Collision col, @NotNull Cylinder cylinder, @NotNull Vector3 vel, float timeToCol) {
        col.timeToCollision = timeToCol;
        col.type = Collision.Type.VERTICAL;

        return col;
    }
    @Nullable
    public Collision getCollision(@NotNull Cylinder cylinder, @NotNull Vector3 vel) {
        Float ts = timeToSideCollision(cylinder, vel);
        Float tv = timeToVerticalCollision(cylinder, vel);
        if (ts == null && tv == null) return null;
        Collision col = Collision.col();
        col.colliding = this;
        col.collided = cylinder;
        col.collidingVel = vel;
        if (tv == null) return getSideCollision(col, cylinder, vel, ts);
        if (ts == null) return getVerticalCollision(col, cylinder, vel, tv);
        if (ts <= tv) return getSideCollision(col, cylinder, vel, ts);
        if (tv <= ts) return getVerticalCollision(col, cylinder, vel, tv);
    }
    @Nullable
    public Collision getCollision(@NotNull PolygonEntity polygon, @NotNull Vector3 vel) {
        return null;
    }
    @Nullable
    @Override
    public Collision getCollision(@NotNull Entity entity, @NotNull Vector3 vel) {
        if (entity instanceof Cylinder) {
            return getCollision((Cylinder)entity, vel);
        }
        if (entity instanceof PolygonEntity) {
            return getCollision((PolygonEntity)entity, vel);
        }
        return null;
    }
}
