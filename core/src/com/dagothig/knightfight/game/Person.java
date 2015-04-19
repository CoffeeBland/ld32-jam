package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.state.VectorPool;

public abstract class Person extends Actor {
    public static final float MIN_DISTANCE = 0.001f;

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

    public float distanceSquared(Person person, Vector3 vel) {
        float dstX = person.pos.x - (pos.x + vel.x);
        float dstY = person.pos.y - (pos.y + vel.y);
        return dstX * dstX + dstY * dstY;
    }
    public boolean withinHeight(Person person, Vector3 vel) {
        float z = pos.z + vel.z;
        return z < (person.pos.z + person.height) && (z + height) > person.pos.z;
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

    @Override
    public void update(float delta, World world) {
        updatePos(delta, world);
        mainTexture.update(delta);
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
        if (pos.z + velocity.z < MIN_DISTANCE) velocity.z = -pos.z;

        if (Math.abs(pos.x) < MIN_DISTANCE && Math.abs(pos.y) < MIN_DISTANCE && Math.abs(pos.z) < MIN_DISTANCE) return;

        movement.set(velocity);
        for (Actor actor : world.actorsLayer.actors) {
            if (!(actor instanceof Person) || actor == this) continue;
            Person person = (Person)actor;
            if (!withinHeight(person, movement)) continue;
            // If we are within the bounds of the person
            float distanceSquared = distanceSquared(person, movement);
            if (distanceSquared < radiusSquared + person.radiusSquared) {
                // If we are above the person
                if (pos.z + movement.z - (person.pos.z + person.height) > -MIN_DISTANCE) {
                    velocity.z = -0.5f;
                    movement.z = Math.max(pos.z - (person.pos.z + person.height), movement.z);
                } // If we are below the person
                else if (pos.z + movement.z + height - person.pos.z < MIN_DISTANCE) {
                    velocity.z = -0.5f;
                    movement.z = Math.min(person.pos.z - (pos.z + height), movement.z);
                } // Sideways collision
                else {
                    Float t = timeToCollision(person, movement);
                    if (t == null || t >= 1 || t <= -1) continue;
                    movement.x *= t;
                    movement.y *= t;
                    float movAngle = angleOf(movement);
                    float colAngle = angleAtCollision(person, movement);
                    float newAngle = 2 * colAngle - movAngle;
                    float velN = (float)Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
                    Vector3 tmpVel = VectorPool.V3().set(velocity);
                    velocity.x = ((velN * -(float)Math.cos(newAngle)) + person.velocity.x) * 0.5f;
                    velocity.y = ((velN * -(float)Math.sin(newAngle)) + person.velocity.y) * 0.5f;
                    person.reactToCollision(this, tmpVel, colAngle + (float)Math.PI);
                    VectorPool.claim(tmpVel);
                }
            }
        }
        pos.add(movement);
    }

    public void reactToCollision(Person person, Vector3 personVel , float colAngle) {
        float movAngle = angleOf(velocity);
        float newAngle = 2 * colAngle - movAngle;
        float velN = (float)Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
        velocity.x = ((velN * -(float)Math.cos(newAngle)) + personVel.x) * 0.5f;
        velocity.y = ((velN * -(float)Math.sin(newAngle)) + personVel.y) * 0.5f;
    }

    @Override
    public void render(SpriteBatch batch, Camera camera, Vector3 pos) {
        Vector2 camPos = camera.getPosition();
        orientation %= Math.PI * 2;
        while (orientation < 0) orientation += Math.PI * 2;

        batch.setColor(shadowColor);
        batch.draw(shadow, pos.x - (shadow.getWidth() / 2) - camPos.x, pos.y - shadow.getHeight() - camPos.y);
        batch.setColor(Color.WHITE);

        mainTexture.setFrameX(getFrameX(orientation));
        mainTexture.renderSheet(batch,
                Math.round(pos.x - mainShiftX) - camPos.x,
                Math.round(pos.y - mainShiftY + pos.z) - camPos.y,
                getImageFlipped(orientation), 1);
    }

    public int getFrameX(float orientation) {
        double partial = orientation / Math.PI * 8;
        if (partial >= 11 && partial < 13) return 0;
        else if (partial >= 13 && partial < 15 || partial >= 9 && partial < 11) return 1;
        else if (partial >= 15 && partial < 0 || partial >= 0 && partial < 1 || partial >= 7 && partial < 9) return 2;
        else if (partial >= 1 && partial < 3 || partial >= 5 && partial < 7) return 3;
        else if (partial >= 3 || partial < 5) return 4;
        else {
            throw new RuntimeException("Orientation was not clamped properly (" + orientation + ")");
        }
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
