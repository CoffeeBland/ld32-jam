package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface WorldLayer {
    void render(SpriteBatch batch);
    void update(float delta);
}
