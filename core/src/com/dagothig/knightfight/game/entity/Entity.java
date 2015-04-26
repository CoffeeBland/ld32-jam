package com.dagothig.knightfight.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;

public abstract class Entity {

    public final Vector3 pos = new Vector3();

    public abstract int getVisualWidth();
    public abstract int getVisualHeight();

    public abstract void update(float delta, World world);
    public void render(SpriteBatch batch) {
        render(batch, pos);
    }
    public abstract void render(SpriteBatch batch, Vector3 pos);
}