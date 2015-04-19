package com.dagothig.knightfight.state;

import com.badlogic.gdx.graphics.Color;

public abstract class AutoSwitchState extends State<Integer> {
    private float remainingTime = getDuration();
    private boolean hasSwitched = false;

    protected abstract float getDuration();
    protected abstract Class<? extends State> getNextStateClass();
    protected abstract Color getTransitionColor();
    protected abstract float getTransitionDuration();

    @Override
    public boolean shouldBeReused() {
        return false;
    }

    @Override
    public void update(float delta) {
        if (!hasSwitched) {
            if (remainingTime >= 0) {
                remainingTime -= delta;
            } else {
                switchToState(getNextStateClass(), getTransitionColor(), getTransitionDuration());
                hasSwitched = true;
            }
        }
    }

    @Override
    public void onTransitionInStart(boolean firstTransition, Integer duration) {
        if (duration == null) {
            remainingTime = getDuration();
        } else {
            remainingTime = duration;
        }
        hasSwitched = false;
    }
}
