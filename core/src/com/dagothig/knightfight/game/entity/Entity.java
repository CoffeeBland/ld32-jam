package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class Entity {

    public final Vector3 pos = new Vector3();
    public boolean canBeCollided;
    public float mass;

    public Entity(boolean canBeCollided, float mass) {
        this.canBeCollided = canBeCollided;
        this.mass = mass;
    }

    public abstract int getVisualWidth();
    public abstract int getVisualHeight();

    public void reactToCollision(@NotNull Collision collision) {}

    public abstract void update(float delta, @NotNull World world);
    public void render(@NotNull SpriteBatch batch) {
        render(batch, pos);
    }
    public abstract void render(@NotNull SpriteBatch batch, @NotNull Vector3 pos);
}