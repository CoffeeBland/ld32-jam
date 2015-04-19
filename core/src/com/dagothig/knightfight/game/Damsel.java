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
    public float speed = 0.5f;

    public Damsel() {
        super(DamselName.getRandomName().name(), new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1));
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
        orientation = (float)Math.atan2(yAxis, xAxis);
        velocity.add(speed * xAxis, speed * yAxis, 0);

        super.update(delta, world);
    }
}
