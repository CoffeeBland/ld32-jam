package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.dagothig.knightfight.res.Definitions;
import com.dagothig.knightfight.res.ImageSheet;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.res.Textures;

/**
 * Created by dagothig on 4/18/15.
 */
public class Damsel extends Person {
    public Color skinColor, robeHighlightColor, hairColor;

    public float xAxis, yAxis;
    public boolean wantsToJump;
    public float speed = 1f;

    public Damsel() {
        super(12, 18,
                DamselName.getRandomName().name(), new Color(
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                (float)Math.random() * 0.5f + 0.5f,
                1
        ));
        skinColor = Color.WHITE.cpy();
        robeHighlightColor = Color.WHITE.cpy();
        hairColor = Color.WHITE.cpy();

        shadow = Textures.get("test_shadow.png");

        mainTexture = new SheetAnimator(new ImageSheet(new ImageSheet.TextureDef(
                "test_lady.png",
                Textures.get("test_lady.png", color),
                Definitions.TEST_LADY.frameWidth,
                Definitions.TEST_LADY.frameHeight
        )), 0, true);
        mainShiftX = mainTexture.getImageSheet().getFrameWidth() / 2;
        mainShiftY = shadow.getHeight() / 2;
    }

    @Override
    public void update(float delta, World world) {
        if (wantsToJump) {
            wantsToJump = false;
            if (pos.z < MIN_DISTANCE) velocity.z += 10;
        }

        orientation = (float)Math.atan2(yAxis, xAxis);
        if (pos.z < MIN_DISTANCE) {
            velocity.add(speed * xAxis, speed * yAxis, 0);
        } else {
            velocity.add(speed * xAxis * 0.1f, speed * yAxis * 0.1f, 0);
        }

        super.update(delta, world);
    }
}
