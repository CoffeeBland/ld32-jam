package com.dagothig.knightfight.res;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dagothig on 8/23/14.
 */
public class SheetAnimator {
    public SheetAnimator(ImageSheet.Definition sheetDef, float fps, boolean loop) {
        this.imageSheet = Textures.get(sheetDef);
        this.loop = loop;
        setFps(fps);
    }

    ImageSheet imageSheet;
    boolean loop;
    int frameX = 0, frameY = 0;
    float fps, frameLength, durationRemaining;

    public int getFrameX() {
        return frameX;
    }
    public void setFrameX(int frameX) {
        this.frameX = frameX;
        durationRemaining = frameLength;
    }
    public int getFrameY() {
        return frameY;
    }
    public void setFrameY(int frameY) {
        this.frameY = frameY;
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

    private int queuedFrameY = -1;
    public void playFrameY(int frameY, int resolveY) {
        queuedFrameY = resolveY;
        this.frameX = 0;
        this.frameY = frameY;
        this.durationRemaining = frameLength;
    }

    public void update(float delta) {
        if (fps != 0) {
            durationRemaining -= delta;
            while (durationRemaining < 0) {
                durationRemaining += frameLength;

                if (frameX == imageSheet.framesX - 1) {
                    if (queuedFrameY != -1) {
                        frameX = 0;
                        frameY = queuedFrameY;
                    } else if (loop) {
                        frameX = 0;
                    }
                } else {
                    frameX++;
                }
            }
        }
    }
}
