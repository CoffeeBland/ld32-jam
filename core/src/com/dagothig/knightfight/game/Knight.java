package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by dagothig on 4/18/15.
 */
public class Knight extends Person {
    public Knight() {
        super(12, 18,
                KnightName.getRandomName().name(), new Color(
                        (float)Math.random() * 0.5f + 0.25f,
                        (float)Math.random() * 0.5f + 0.25f,
                        (float)Math.random() * 0.5f + 0.25f,
                        1
        ));
    }
}
