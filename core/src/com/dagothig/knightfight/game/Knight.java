package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.dagothig.knightfight.res.Definitions;
import com.dagothig.knightfight.res.ImageSheet;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.res.Textures;

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

        shadow = Textures.get("lady_shadow.png");

        mainTexture = new SheetAnimator(new ImageSheet(new ImageSheet.TextureDef(
                "test_lady.png",
                Textures.get("test_lady.png", color),
                Definitions.LADY_SHEET.frameWidth,
                Definitions.LADY_SHEET.frameHeight
        )), 0, true);
        mainShiftX = mainTexture.getImageSheet().getFrameWidth() / 2;
        mainShiftY = shadow.getHeight() / 2;
    }
}
