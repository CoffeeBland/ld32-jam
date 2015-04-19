package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.res.SheetAnimator;

public abstract class Person extends Actor {
    public String name;
    public Color color;

    public SheetAnimator mainTexture;
    public Texture shadow;
    public Color shadowColor = new Color(0, 0, 0, 0.3f);

    // Radian angle (0 - 2PI)
    public float orientation;

    public float mainShiftX, mainShiftY;
    public final Vector3 velocity = new Vector3();
    protected final Vector3 movement = new Vector3();

    public Person(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public void update(float delta, World world) {
        velocity.scl(world.friction);
        pos.add(movement.set(velocity));

        mainTexture.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, Vector3 pos) {
        orientation %= Math.PI * 2;
        while (orientation < 0) orientation += Math.PI * 2;

        batch.setColor(shadowColor);
        batch.draw(shadow, pos.x - shadow.getWidth() / 2, pos.y - shadow.getHeight());
        batch.setColor(Color.WHITE);

        mainTexture.setFrameY(getFrameY(orientation));
        mainTexture.renderSheet(batch, pos.x - mainShiftX, pos.y - mainShiftY + pos.z, getImageFlipped(orientation), 1);
    }

    public int getFrameY(float orientation) {
        return 0; // TODO: get frameY
    }
    public boolean getImageFlipped(float orientation) {
        return orientation >= (Math.PI / 2) && orientation < 3 * (Math.PI / 2);
    }

    @Override
    public int getWidth() {
        return mainTexture.getImageSheet().getFrameWidth();
    }
    @Override
    public int getHeight() {
        return mainTexture.getImageSheet().getFrameWidth();
    }
}
