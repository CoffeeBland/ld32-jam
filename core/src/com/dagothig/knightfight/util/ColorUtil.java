package com.dagothig.knightfight.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ColorUtil {

    public static Texture whitePixel() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.drawPixel(0, 0, 0xFFFFFFFF);
        return new Texture(bgPixmap);
    }

}
