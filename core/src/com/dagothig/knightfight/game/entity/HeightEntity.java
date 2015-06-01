package com.dagothig.knightfight.game.entity;

/**
 * Created by dagothig on 5/10/15.
 */
public abstract class HeightEntity extends Entity {

    public float height;

    public HeightEntity(boolean canBeCollided, float mass, float height) {
        super(canBeCollided, mass);
        this.height = height;
    }
}
