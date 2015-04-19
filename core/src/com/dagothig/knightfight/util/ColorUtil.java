package com.dagothig.knightfight.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ColorUtil {

    public static Texture whitePixel() {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.drawPixel(0, 0, 0xFFFFFFFF);
        Texture text = new Texture(bgPixmap);
        bgPixmap.dispose();
        return text;
    }
    public static int toIntBitsProper (Color color) {
        return
                ((int)(255 * color.r) << 24)
                | ((int)(255 * color.g) << 16)
                | ((int)(255 * color.b) << 8)
                | ((int)(255 * color.a) << 0)
        ;
    }
}
