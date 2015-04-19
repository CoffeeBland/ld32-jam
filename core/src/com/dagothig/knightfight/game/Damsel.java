package com.dagothig.knightfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.dagothig.knightfight.res.Definitions;
import com.dagothig.knightfight.res.ImageSheet;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.res.Textures;
import com.dagothig.knightfight.util.Pair;

public class Damsel extends Person {

    private static final int
            ANIMATION_WALK = 0,
            ANIMATION_PICKUP = 1,
            ANIMATION_HOLD = 2,
            ANIMATION_THROW = 3;

    public Knight knight;
    public Color skinColor, robeHighlightColor, hairColor;

    public float xAxis, yAxis;
    public boolean wantsToJump, wantsToThrow;
    public float speed = 2f, jumpStrength = 20f;

    public Damsel() {
        super(80, 160,
                DamselName.getRandomName().name(), new Color(
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                1
        ));
        skinColor = new Color(0.9f * (float)Math.random(), 0.9f * (float)Math.random(), 0.8f * (float)Math.random(), 1);
        robeHighlightColor = color.cpy().mul(1.2f);
        hairColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);

        shadow = Textures.get("lady_shadow.png");

        Texture skin = Textures.get("01_peau.png");
        Texture dress = Textures.get("02_robe.png");
        Texture hair = Textures.get("03_cheveux.png");
        Texture eyes = Textures.get("04_yeux.png");
        Texture dressExtra = Textures.get("05_volants.png");

        // Render to a framebuffer
        SpriteBatch batch = new SpriteBatch();
        FrameBuffer fb = new FrameBuffer(Pixmap.Format.RGBA8888, dress.getWidth(), dress.getHeight(), true);
        batch.enableBlending();
        batch.setBlendFunction(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, fb.getWidth(), fb.getHeight()));
        fb.begin();
        batch.begin();

        // Because the framebuffer apparently does y flip of the textures, we flip every render call
        batch.setColor(skinColor);
        batch.draw(skin, 0, 0, skin.getWidth(), skin.getHeight(), 0, 0, skin.getWidth(), skin.getHeight(), false, true);
        batch.setColor(color);
        batch.draw(dress, 0, 0, dress.getWidth(), dress.getHeight(), 0, 0, dress.getWidth(), dress.getHeight(), false, true);
        batch.setColor(hairColor);
        batch.draw(hair, 0, 0, hair.getWidth(), hair.getHeight(), 0, 0, hair.getWidth(), hair.getHeight(), false, true);
        batch.setColor(Color.WHITE);
        batch.draw(eyes, 0, 0, eyes.getWidth(), eyes.getHeight(), 0, 0, eyes.getWidth(), eyes.getHeight(), false, true);
        batch.setColor(robeHighlightColor);
        batch.draw(dressExtra, 0, 0, dressExtra.getWidth(), dressExtra.getHeight(), 0, 0, dressExtra.getWidth(), dressExtra.getHeight(), false, true);
        batch.setColor(Color.WHITE);

        batch.end();
        fb.end();

        // Apparently, fb.dispose() makes its texture dispose as well. So we aren't doing it
        mainTexture = new SheetAnimator(
                new ImageSheet(new ImageSheet.TextureDef(
                        "01_peau.png",
                        fb.getColorBufferTexture(),
                        Definitions.LADY_SHEET.frameWidth,
                        Definitions.LADY_SHEET.frameHeight
                )),
                8, true,
                new Pair<>(0, 2),
                new Pair<>(3, 5),
                new Pair<>(6, 8),
                new Pair<>(4, 4)
        );
        batch.dispose();
        //fb.dispose();

        mainShiftX = mainTexture.getImageSheet().getFrameWidth() / 2;
        mainShiftY = shadow.getHeight() / 2 + 32;
    }

    @Override
    public void update(float delta, World world) {
        if (wantsToThrow) {
            if (knight == null) { // Pickup
                mainTexture.playAnimation(ANIMATION_PICKUP, ANIMATION_HOLD);
                mainTexture.setFps(8);
                knight = new Knight();
            } else if (knight != null) {
                mainTexture.playAnimation(ANIMATION_THROW, ANIMATION_WALK);
                mainTexture.setFps(8);
                knight = null;
            }
            wantsToThrow = false;
        }
        if (wantsToJump) {
            wantsToJump = false;
            if (pos.z < MIN_DISTANCE) velocity.z += jumpStrength;
        }

        if (xAxis != 0 || yAxis != 0) orientation = (float)Math.atan2(yAxis, xAxis);
        if (pos.z < MIN_DISTANCE) {
            velocity.add(speed * xAxis, speed * yAxis, 0);
        } else {
            velocity.add(speed * xAxis * 0.1f, speed * yAxis * 0.1f, 0);
        }

        switch (mainTexture.getAnimationId()) {
            case ANIMATION_WALK:
                mainTexture.setFps(Math.max(velocity.len() * 4, 3));
                break;
            case ANIMATION_HOLD:
                mainTexture.setFps(Math.max(velocity.len() * 4, 3) * 0.8f);
                break;
        }

        super.update(delta, world);
    }
}
