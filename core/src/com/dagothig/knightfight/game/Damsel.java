package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by dagothig on 4/18/15.
 */
public class Damsel extends Person {
    public Color skinColor, robeHighlightColor, hairColor;

    @Override
    public String getDefaultName() {
        return DamselName.getRandomName().name();
    }

    @Override
    public Color getDefaultColor() {
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
    }

    public Damsel() {
        super();
        skinColor = Color.WHITE.cpy();
        robeHighlightColor = Color.WHITE.cpy();
        hairColor = Color.WHITE.cpy();
    }
}
