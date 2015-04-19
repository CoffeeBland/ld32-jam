package com.dagothig.knightfight.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KnightFightKbdController extends KnightFightController implements InputProcessor {
    public KnightFightKbdController() {
        Gdx.input.setInputProcessor(this);
    }

    protected boolean handleKey(int keycode, boolean isDown) {
        float axisValue = 0f;
        KnightFightMappings.Axis activeAxis = null;
        KnightFightMappings.Button activeButton = null;
        switch (keycode) {
            case Input.Keys.W:
                activeAxis = KnightFightMappings.Axis.LEFT_HORIZONTAL;
                axisValue = 1f;
                break;
        }
        for (KnightFightControllerListener l : this.listeners) {
            if (activeAxis != null) l.axisMoved(this, activeAxis, axisValue);
            if (activeButton != null && isDown) l.buttonDown(this, activeButton);
            if (activeButton != null && !isDown) l.buttonUp(this, activeButton);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return handleKey(keycode, true);
    }

    @Override
    public boolean keyUp(int keycode) {
        return handleKey(keycode, false);
    }

    @Override public boolean keyTyped(char character) { return false; }

    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override public boolean scrolled(int amount) { return false; }
}
