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
        float vpWidth = Gdx.graphics.getWidth();
        float vpHeight = Gdx.graphics.getHeight();
        batch.draw(img, (vpWidth/2)-(img.getWidth()/2), (vpHeight/2)-(img.getHeight()/2));
    }

    @Override
    public void update(float delta, World world) {

    }
}
