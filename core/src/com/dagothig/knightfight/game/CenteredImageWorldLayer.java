package com.dagothig.knightfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CenteredImageWorldLayer implements WorldLayer {
    Texture img;

    public CenteredImageWorldLayer(String filename) {
        img = new Texture(Gdx.files.internal(filename));
    }

    @Override
    public void render(SpriteBatch batch, Camera camera) {
        Vector2 origin = camera.getPosition();
        batch.draw(img, origin.x - (img.getWidth()/2), origin.y - (img.getHeight()/2));
    }

    @Override
    public void update(float delta, World world) {

    }
}
