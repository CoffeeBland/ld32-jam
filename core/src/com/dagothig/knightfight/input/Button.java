package com.dagothig.knightfight.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dagothig on 11/2/14.
 */
public abstract class Button implements Clickable {
    boolean isInactive, isDown, isHovered;

    public State getState() {
        if (isInactive) {
            return State.INACTIVE;
        } else if (isDown) {
            if (isHovered) {
                return State.DOWN;
            } else {
                return State.HOVERED;
            }
        } else if (isHovered) {
            return State.HOVERED;
        } else {
            return State.NORMAL;
        }
    }

    public abstract boolean isMouseWithin(int x, int y);
    public abstract boolean isButtonValid(int button);

    public abstract void onTriggered();

    @Override
    public float getClickPriority() {
        return getRenderPriority();
    }
    public abstract float getRenderPriority();

    @Override
    public boolean onMouseDown(int button, int x, int y) {
        if (isInactive) {
            return false;
        }

        if (isMouseWithin(x,y) && isButtonValid(button)) {
            isDown = true;

            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseUp(int button, int x, int y) {
        if (isInactive) {
            return false;
        }

        isDown = false;
        if (isMouseWithin(x, y)) {
            if (isButtonValid(button)) {
                onTriggered();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onMouseMove(int x, int y) {
        if (isInactive) {
            return false;
        }

        if (isMouseWithin(x, y)) {
            isHovered = true;
            return true;
        }

        return false;
    }

    @Override
    public void update(float delta) {
        isHovered = false;
    }

    public static enum State {
        INACTIVE, DOWN, HOVERED, NORMAL;

        public State getPrioritisedState(State state) {
            if (state.ordinal() < ordinal()) {
                return state;
            } else {
                return this;
            }
        }
    }

    public abstract void render(SpriteBatch batch);
}
