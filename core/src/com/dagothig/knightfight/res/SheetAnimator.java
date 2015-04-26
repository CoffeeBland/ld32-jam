package com.dagothig.knightfight.res;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.util.Pair;

/**
 * Created by dagothig on 8/23/14.
 */
public class SheetAnimator {

    protected Pair<Integer, Integer>[] animations;
    protected int animationId;
    protected final ImageSheet imageSheet;
    protected boolean loop;
    protected boolean loopingBack;
    protected int frameX = 0, frameY = 0, step = 1;
    protected float fps, frameLength, durationRemaining;
    protected Listener listener;

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

    public Listener getListener() { return listener; }
    public void setListener(Listener listener) { this.listener = listener; }

    public void setFps(float fps) {
        if (fps <= 0) {
            this.fps = 0;
        } else {
            frameLength = 1000 / fps;
            this.fps = fps;
        }
    }
    public void resetDurationRemaining() {
        durationRemaining = frameLength;
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
        this.step = animations[animationId].second >= animations[animationId].first ? 1 : -1;
        this.durationRemaining = frameLength;
    }
    public void playAnimation(int animationId, int resolveId) {
        queuedFrameX = frameX;
        queuedAnimationId = resolveId;
        this.frameY = animations[this.animationId = animationId].first;
        this.step = animations[animationId].second >= animations[animationId].first ? 1 : -1;
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
                        if (listener != null) listener.onAnimationFinished(animationId, queuedAnimationId);
                        frameY = animations[animationId = queuedAnimationId].first;
                        this.step = animations[animationId].second >= animations[animationId].first ? 1 : -1;
                    } else if (loop) {
                        loopingBack = true;
                        frameY -= step;
                    }
                } else if (frameY == animations[animationId].first) {
                    if (loop) {
                        loopingBack = false;
                        frameY += step;
                    }
                } else {
                    frameY += loopingBack ? -step : step;
                }
            }
        }
    }

    public interface Listener {
        void onAnimationFinished(int previousId, int nextId);
    }
}
