package com.dagothig.knightfight.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.res.Textures;

/**
 * Created by dagothig on 4/18/15.
 */
public class SplashState extends AutoSwitchState {
    @Override protected float getDuration() { return 2000; }
    @Override protected Class<? extends State> getNextStateClass() { return GameState.class; }
    @Override protected Color getTransitionColor() { return new Color(0xefe8e0ff); }
    @Override protected float getTransitionDuration() { return TRANSITION_MEDIUM; }

    Texture text;

    @Override
    public void render(SpriteBatch batch) {
        if (text == null) text = Textures.get("badlogic.jpg");
        batch.draw(text, 0, 0);
    }
}