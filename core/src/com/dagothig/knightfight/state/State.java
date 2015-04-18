package com.dagothig.knightfight.state;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State<StateArgs> implements InputProcessor {
    public static final float TRANSITION_SHORT = 250;
    public static final float TRANSITION_MEDIUM = 500;
    public static final float TRANSITION_LONG = 1000;

    public State() {}

    protected StateManager stateManager;
    protected Color backgroundColor;

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void switchToState(Class<? extends State> stateType, Color transitionColor, float transitionLength, Object switchArgs) {
        stateManager.switchToState(stateType, transitionColor, transitionLength, switchArgs);
    }
    public void switchToState(Class<? extends State> stateType, Color transitionColor, float transitionLength) {
        stateManager.switchToState(stateType, transitionColor, transitionLength);
    }

    public Color getBackgroundColor() {
        if (backgroundColor == null) {
            backgroundColor = Color.BLACK.cpy();
        }
        return backgroundColor;
    }
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public abstract boolean shouldBeReused();
    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

    public void onTransitionInStart(boolean firstTransition, StateArgs args) {}
    public void onTransitionInFinish() {}
    public void onTransitionOutStart() {}
    public void onTransitionOutFinish() {}

    @Override
    public boolean keyDown(int keycode) {
        return false; }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
