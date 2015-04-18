package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by dagothig on 4/18/15.
 */
public class Knight extends Person {
    @Override
    public String getDefaultName() {
        return KnightName.getRandomName().name();
    }

    @Override
    public Color getDefaultColor() {
        return Color.WHITE.cpy();
    }
}
