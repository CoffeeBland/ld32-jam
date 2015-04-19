package com.dagothig.knightfight.res;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dagothig on 8/23/14.
 */
public class ImageSheet {
    public ImageSheet(Definition def) {
        this.ref = def.ref;
        this.texture = def.getTexture();
        this.frameWidth = def.frameWidth;
        this.frameHeight = def.frameHeight;
        this.framesX = texture.getWidth() / frameWidth;
        this.framesY = texture.getHeight() / frameHeight;
    }
    protected final String ref;
    protected final int framesX, frameWidth, framesY, frameHeight;
    protected final Texture texture;
    protected Pixmap pixmap;

    public void initPixmap() {
        if (pixmap != null) {
            return;
        }
        pixmap = new Pixmap(new FileHandle(ref));
    }

    public int getFrameWidth() {
        return frameWidth;
    }
    public int getFrameHeight() {
        return frameHeight;
    }

    public void render(SpriteBatch batch, float x, float y, int imageX, int imageY, float scale, boolean flip) {
        batch.draw(texture,
                x, y,
                frameWidth * scale, frameHeight * scale,
                frameWidth * imageX,
                frameHeight * imageY,
                frameWidth, frameHeight,
                flip, false
        );
    }
    public void render(SpriteBatch batch, float x, float y, int imageX, int imageY, float scale, boolean flip, Color tint) {
        batch.setColor(tint);
        render(batch, x, y, imageX, imageY, scale, flip);
        batch.setColor(Color.WHITE);
    }
    public void render(Pixmap target, float x, float y, int imageX, int imageY) {
        initPixmap();

        target.drawPixmap(pixmap,
                imageX * getFrameWidth(), imageY * getFrameHeight(),
                getFrameWidth(), getFrameHeight(),
                (int)x, (int)y,
                getFrameWidth(), getFrameHeight()
        );
    }

    public static class Definition {
        public String ref;

        public int frameWidth, frameHeight;

        public Definition(String ref, int frameWidth, int frameHeight) {
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
            this.ref = ref;
        }
        public Texture getTexture() {
            return Textures.get(ref);
        }
    }
    public static class TextureDef extends Definition {
        Texture texture;

        public TextureDef(String ref, Texture texture, int frameWidth, int frameHeight) {
            super(ref, frameWidth, frameHeight);
            this.texture = texture;
        }

        @Override
        public Texture getTexture() {
            return texture;
        }
    }
}
