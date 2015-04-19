package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface WorldLayer {
    void render(SpriteBatch batch, Camera camera);
    void update(float delta, World world);
}
