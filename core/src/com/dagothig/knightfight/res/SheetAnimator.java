package com.dagothig.knightfight.res;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.util.Pair;

/**
 * Created by dagothig on 8/23/14.
 */
public class SheetAnimator {

    Pair<Integer, Integer>[] animations;
    int animationId;
    final ImageSheet imageSheet;
    boolean loop;
    int frameX = 0, frameY = 0;
    float fps, frameLength, durationRemaining;

    public SheetAnimator(ImageSheet.Definition sheetDef, float fps, boolean loop, Pair<Integer, Integer>... animations) {
        this(Textures.get(sheetDef), fps, loop, animations);
    }
    public SheetAnimator(ImageSheet imageSheet, float fps, boolean loop, Pair<Integer, Integer>... animations) {
        this.imageSheet = imageSheet;
        this.loop = loop;
        this.animations = animations;
        this.animationId = 0;
        setFps(fps);
    }

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

    public int getAnimationId() {
        return animationId;
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

    private int queuedFrameX = -1, queuedAnimationId = -1;
    public void playAnimation(int frameX, int animationId, int resolveX, int resolveId) {
        queuedFrameX = resolveX;
        queuedAnimationId = resolveId;
        this.frameX = frameX;
        this.frameY = animations[this.animationId = animationId].first;
        this.durationRemaining = frameLength;
    }
    public void playAnimation(int animationId, int resolveId) {
        queuedFrameX = frameX;
        queuedAnimationId = resolveId;
        this.frameY = animations[this.animationId = animationId].first;
        this.durationRemaining = frameLength;
    }

    public void update(float delta) {
        if (fps != 0) {
            durationRemaining -= delta;
            while (durationRemaining < 0) {
                durationRemaining += frameLength;

                if (frameY == animations[animationId].second) {
                    if (queuedAnimationId != -1) {
                        frameX = queuedFrameX;
                        frameY = animations[animationId = queuedAnimationId].first;
                    } else if (loop) {
                        frameY = animations[animationId].first;
                    }
                } else {
                    frameY++;
                }
            }
        }
    }
}
