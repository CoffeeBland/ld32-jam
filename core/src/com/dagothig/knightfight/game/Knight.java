package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.dagothig.knightfight.res.Definitions;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.res.Textures;
import com.dagothig.knightfight.util.Pair;

/**
 * Created by dagothig on 4/18/15.
 */
public class Knight extends Person {
    public Knight() {
        super(32, 200,
                KnightName.getRandomName().name(), new Color(
                        (float)Math.random() * 0.5f + 0.25f,
                        (float)Math.random() * 0.5f + 0.25f,
                        (float)Math.random() * 0.5f + 0.25f,
                        1
        ));
        mainTexture = new SheetAnimator(Definitions.KNIGHT, 12, true, new Pair<>(0, 2));
        shadow = Textures.get("knight_shadow.png");

        mainShiftX = mainTexture.getImageSheet().getFrameWidth() / 2;
        mainShiftY = shadow.getHeight() / 2 - 14;
    }

    @Override
    public int getFrameX(float orientation) {
        return 0;
    }
}
