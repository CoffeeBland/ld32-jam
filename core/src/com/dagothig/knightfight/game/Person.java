package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.entity.Actor;
import com.dagothig.knightfight.game.entity.Entity;
import com.dagothig.knightfight.game.world.PolygonLine;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.util.VectorPool;
import org.jetbrains.annotations.NotNull;

public abstract class Person extends Actor {
    public static final float
            MIN_DISTANCE = 0.001f,
            INERTIA = 0.1f;

    public String name;
    public Color color;

    public SheetAnimator mainTexture;
    public Texture shadow;
    public Color shadowColor = new Color(0, 0, 0, 0.3f);

    // Radian angle (0 - 2PI)
    public float orientation;

    public float radius, radiusSquared, height;

    public float mainShiftX, mainShiftY;
    public final Vector3 velocity = new Vector3();
    protected final Vector3 movement = new Vector3();


    public Person(float radius, float height, String name, Color color) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.height = height;
        this.name = name;
        this.color = color;
    }

    // === Person collision calculations === //
    public float distanceSquared(Person person, Vector3 vel) {
        float dstX = person.pos.x - (pos.x + vel.x);
        float dstY = person.pos.y - (pos.y + vel.y);
        return dstX * dstX + dstY * dstY;
    }
    public Float timeToCollision(Person person, Vector3 vel) {
        /*
        On pose r3 = r1² + r2²
        xd = x1 - x2
        yd = y1 - y2
        On cherche moment où la distance entre les deux cercles est égale à 0,
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
        float r3 = radiusSquared + person.radiusSquared;
        float xd = pos.x - person.pos.x;
        float yd = pos.y - person.pos.y;
        float a = vel.x * vel.x + vel.y * vel.y;
        float b = 2 * xd * vel.x + 2 * yd * vel.y;
        float c = xd * xd + yd * yd - r3;
        if (a == 0) return null;
        float discriminantSquared = b * b - 4 * a * c;
        if (discriminantSquared < 0) return null;
        float discriminant = (float)Math.sqrt(discriminantSquared);
        float t1 = (-b + discriminant) / (2 * a);
        float t2 = (-b - discriminant) / (2 * a);
        return Math.min(t1, t2);
    }
    public float angleOf(Vector3 vec) {
        return (float)Math.atan2(vec.y, vec.x);
    }
    public float angleAtCollision(Person person, Vector3 vel) {
        Vector3 velPos = VectorPool.V3();
        velPos.set(pos).add(vel);
        float angle = (float)Math.atan2(velPos.y - person.pos.y, velPos.x - person.pos.x);
        VectorPool.claim(velPos);
        return angle;
    }

    public boolean isWithinDistance(Person person, Vector3 vel) {
        return distanceSquared(person, vel) - (radiusSquared + person.radiusSquared) < MIN_DISTANCE;
    }
    public boolean isWithinDistanceAtPos(Person person, Vector3 pos) {
        return Vector2.dst2(pos.x, pos.y, person.pos.x, person.pos.y) - (radiusSquared + person.radiusSquared) < MIN_DISTANCE;
    }
    public boolean isWithinHeight(Person person) {
        return pos.z - (person.pos.z + person.height) < MIN_DISTANCE && pos.z + height - person.pos.z > -MIN_DISTANCE;
    }
    public boolean isWithinHeightAtPos(Person person, Vector3 pos) {
        return pos.z - (person.pos.z + person.height) < MIN_DISTANCE && (pos.z + height) - person.pos.z > -MIN_DISTANCE;
    }
    public boolean goingIntoFromAbove(Person person, Vector3 vel) {
        return vel.z < MIN_DISTANCE
                && pos.z - (person.pos.z + person.height) > -MIN_DISTANCE
                && pos.z + vel.z - (person.pos.z + person.height) < MIN_DISTANCE;
    }
    public boolean goingIntoFromUnder(Person person, Vector3 vel) {
        return vel.z > -MIN_DISTANCE
                && (pos.z + height) - person.pos.z < MIN_DISTANCE
                && (pos.z + height) + vel.z - person.pos.z > -MIN_DISTANCE;
    }

    public boolean isInCollision(World world, Vector3 vel) {
        Vector3 newPos = VectorPool.V3().set(pos).add(vel);

        boolean hadCol = false;
        for (Entity entity : world.entitiesLayer.entities) {
            if (!(entity instanceof Person) || entity == this) continue;
            Person person = (Person) entity;

            if (isWithinDistanceAtPos(person, newPos) && isWithinHeightAtPos(person, newPos)) {
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
    public Float timeToCollision(PolygonLine line, Vector3 vec) {
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


        movement.set(velocity);
        // Inertia
        if (movement.x < INERTIA && movement.x > -INERTIA) movement.x = 0;
        if (movement.y < INERTIA && movement.y > -INERTIA) movement.y = 0;
        if (movement.z < INERTIA && movement.z > -INERTIA) movement.z = 0;

        // Don't check for very small deltas (just not worth it)
        if (Math.abs(movement.x) < MIN_DISTANCE && Math.abs(movement.y) < MIN_DISTANCE && Math.abs(movement.z) < MIN_DISTANCE) return;

        for (Entity entity : world.entitiesLayer.entities) {
            // We only check collisions with other people (this is high society after all!)
            if (!(entity instanceof Person) || entity == this) continue;
            Person person = (Person) entity;

            if (isWithinDistance(person, movement)) {
                if (isWithinHeight(person)) {
                    Float t = timeToCollision(person, movement);
                    if (t == null || t >= 1 || t <= -1) continue;
                    movement.x *= t;
                    movement.y *= t;
                    float movAngle = angleOf(movement);
                    float colAngle = angleAtCollision(person, movement);
                    float newAngle = 2 * colAngle - movAngle;
                    float velN = Vector2.len(velocity.x, velocity.y);
                    Vector3 tmpVel = VectorPool.V3().set(velocity);
                    velocity.x = ((velN * -(float)Math.cos(newAngle)) + person.velocity.x) * 0.5f;
                    velocity.y = ((velN * -(float)Math.sin(newAngle)) + person.velocity.y) * 0.5f;
                    person.reactToCollision(this, tmpVel, colAngle + (float)Math.PI);
                    VectorPool.claim(tmpVel);
                }
                if (goingIntoFromAbove(person, movement)) {
                    // We are above
                    person.reactToCollision(this, velocity);
                    reactToCollision(person, person.velocity);
                    movement.z = 0;
                } else if (goingIntoFromUnder(person, movement)) {
                    // We are below
                    person.reactToCollision(this, velocity);
                    reactToCollision(person, person.velocity);
                    movement.z = 0;
                }
            }
        }
        pos.add(movement);
    }

    @Override
    public void update(float delta, @NotNull World world) {
        updatePos(delta, world);
        mainTexture.update(delta);
    }
    @Override
    public void render(@NotNull SpriteBatch batch, @NotNull Vector3 pos) {
        orientation %= Math.PI * 2;
        while (orientation < 0) orientation += Math.PI * 2;

        batch.setColor(shadowColor);
        batch.draw(shadow,
                Math.round(pos.x - shadow.getWidth() / 2),
                Math.round(pos.y - shadow.getHeight() / 2)
        );
        batch.setColor(Color.WHITE);

        mainTexture.setFrameX(getFrameX(orientation));
        renderMain(batch, pos);
    }
    public void renderMain(SpriteBatch batch, Vector3 pos) {
        mainTexture.renderSheet(batch,
                Math.round(pos.x - mainShiftX),
                Math.round(pos.y - mainShiftY + pos.z),
                getImageFlipped(orientation), 1);
    }

    public int getFrameX(float orientation) {
        double partial = orientation / Math.PI * 8;
        // Because our 0 is right and its index is 2, we offset by a 3/4 turn
        // Then, we mod 16 to have all of our values [0, 16] and absolute the value minus 8 to get a function that is
        // 0 at bottom (formerly 12) and 8 at top (formerly 4)
        // finally, because our indices are 0 - 4, we divide by 2
        return (int)Math.round((Math.abs((partial + 12) % 16 - 8)) / 2);
    }
    public boolean getImageFlipped(float orientation) {
        return orientation >= 5 * (Math.PI / 8) && orientation < 11 * (Math.PI / 8);
    }

    @Override
    public int getVisualWidth() {
        return mainTexture.getImageSheet().getFrameWidth();
    }
    @Override
    public int getVisualHeight() {
        return mainTexture.getImageSheet().getFrameWidth();
    }
}
