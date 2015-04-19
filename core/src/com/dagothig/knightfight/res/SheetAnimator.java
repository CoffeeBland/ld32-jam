package com.dagothig.knightfight.res;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dagothig on 8/23/14.
 */
public class SheetAnimator {
    public SheetAnimator(ImageSheet.Definition sheetDef, float fps, boolean loop) {
        this(Textures.get(sheetDef), fps, loop);
    }
    public SheetAnimator(ImageSheet imageSheet, float fps, boolean loop) {
        this.imageSheet = imageSheet;
        this.loop = loop;
        setFps(fps);
    }

    final ImageSheet imageSheet;
    boolean loop;
    int frameX = 0, frameY = 0;
    float fps, frameLength, durationRemaining;

    public ImageSheet getImageSheet() {
        return imageSheet;
    }

    public int getFrameX() {
        return frameX;
    }
    public void setFrameX(int frameX) {
        this.frameX = frameX;
    }
    public int getFrameY() {
        return frameY;
    }
    public void setFrameY(int frameY) {
        this.frameY = frameY;
        durationRemaining = frameLength;
    }

    public void setFps(float fps) {
        if (fps <= 0) {
            this.fps = 0;
        } else {
            frameLength = 1000 / fps;
            this.fps = fps;
        }
    }

    public void renderSheet(SpriteBatch batch, float x, float y, boolean flip, float scale) {
        imageSheet.render(batch, x, y, frameX, frameY, scale, flip);
    }
    public void renderSheet(SpriteBatch batch, float x, float y, boolean flip, float scale, Color tint) {
        imageSheet.render(batch, x, y, frameX, frameY, scale, flip, tint);
    }

    private int queuedFrameX = -1, queuedFrameY = -1;
    public void playFrameY(int frameX, int frameY, int resolveX, int resolveY) {
        queuedFrameX = resolveX;
        queuedFrameY = resolveY;
        this.frameX = frameX;
        this.frameY = frameY;
        this.durationRemaining = frameLength;
    }

    public void update(float delta) {
        if (fps != 0) {
            durationRemaining -= delta;
            while (durationRemaining < 0) {
                durationRemaining += frameLength;

                if (frameY == imageSheet.framesY - 1) {
                    if (queuedFrameY != -1) {
                        frameX = queuedFrameX;
                        frameY = 0;
                    } else if (loop) {
                        frameY = 0;
                    }
                } else {
                    frameY++;
                }
            }
        }
    }
}
