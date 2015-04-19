package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;

public abstract class Person extends Actor {
    public String name;
    public Color color;

    public abstract String getDefaultName();
    public abstract Color getDefaultColor();

    public Person() {
        name = getDefaultName();
        color = getDefaultColor();
    }
}
