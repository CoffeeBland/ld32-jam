package com.dagothig.knightfight.res;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import static com.dagothig.knightfight.util.ColorUtil.toIntBitsProper;

/**
 * Created by dagothig on 11/2/14.
 */
public class Textures {
    public static final Texture WHITE_PIXEL;

    private static final Map<String, SoftReference<Texture>> images = new HashMap<>();
    public static Texture get(String ref) {

        Texture texture;
        if (images.containsKey(ref)) {
            SoftReference<Texture> sref = images.get(ref);

            texture = sref.get();
            if (texture == null) {
                images.remove(ref);
                return get(ref);
            }
        } else {
            texture = new Texture(new FileHandle(ref));
            images.put(ref, new SoftReference<>(texture));
        }

        return texture;
    }

    public static Texture get(String ref, Color color) {
        Pixmap source = new Pixmap(new FileHandle(ref));
        Pixmap target = new Pixmap(source.getWidth(), source.getHeight(), source.getFormat());
        Color tmpCol = new Color();
        for (int x = 0, nx = source.getWidth(); x < nx; x++) {
            for (int y = 0, ny = source.getHeight(); y < ny; y++) {
                target.drawPixel(x, y, toIntBitsProper(tmpCol.set(source.getPixel(x, y)).mul(color)));
            }
        }
        return new Texture(target);
    }

    private static final Map<ImageSheet.Definition, SoftReference<ImageSheet>> imageSheets = new HashMap<>();
    public static ImageSheet get(ImageSheet.Definition definition) {
        ImageSheet imageSheet;
        if (imageSheets.containsKey(definition)) {
            SoftReference<ImageSheet> sref = imageSheets.get(definition);

            imageSheet = sref.get();
            if (imageSheet == null) {
                imageSheets.remove(definition);
                return get(definition);
            }
        } else {
            imageSheet = new ImageSheet(definition);
            imageSheets.put(definition, new SoftReference<>(imageSheet));
        }

        return imageSheet;
    }

    static {
        Pixmap whitePixelPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixelPixmap.drawPixel(0, 0, 0xFFFFFFFF);
        WHITE_PIXEL = new Texture(whitePixelPixmap);
    }

    private Textures(){}
}
