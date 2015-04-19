package com.dagothig.knightfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CenteredImageWorldLayer implements WorldLayer {
    Texture img;

    public CenteredImageWorldLayer(String filename) {
        img = new Texture(Gdx.files.internal(filename));
    }

    @Override
    public void render(SpriteBatch batch) {
        float centerX = (Gdx.graphics.getWidth() / 2);
        float centerY = (Gdx.graphics.getHeight() / 2);

        batch.draw(img, centerX - (img.getWidth() / 2), centerY - (img.getHeight() / 2));
    }

    @Override
    public void update(float delta, World world) {

    }
}
