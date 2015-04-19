package com.dagothig.knightfight.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

public class KnightFightKbdController extends KnightFightController implements InputProcessor {
    protected boolean handleKey(int keycode, boolean isDown) {
        float axisValue = 0f;
        KnightFightMappings.Axis activeAxis = null;
        List<KnightFightMappings.Button> activeButtons = new ArrayList<>();

        switch (keycode) {
            case Input.Keys.W:
                activeAxis = KnightFightMappings.Axis.LEFT_VERTICAL;
                axisValue = (isDown ? 1f : 0);
                break;
            case Input.Keys.A:
                activeAxis = KnightFightMappings.Axis.LEFT_HORIZONTAL;
                axisValue = (isDown ? -1f : 0);
                break;
            case Input.Keys.S:
                activeAxis = KnightFightMappings.Axis.LEFT_VERTICAL;
                axisValue = (isDown ? -1f : 0);
                break;
            case Input.Keys.D:
                activeAxis = KnightFightMappings.Axis.LEFT_HORIZONTAL;
                axisValue = (isDown ? 1f : 0);
                break;
            case Input.Keys.SPACE:
                activeButtons.add(KnightFightMappings.Button.JUMP);
                activeButtons.add(KnightFightMappings.Button.JOIN);
                break;
            case Input.Keys.ENTER:
                activeButtons.add(KnightFightMappings.Button.START);
                break;
            case Input.Keys.SHIFT_LEFT:
                activeButtons.add(KnightFightMappings.Button.THROW);
                break;
        }

        for (KnightFightControllerListener l : this.listeners) {
            if (activeAxis != null) l.axisMoved(this, activeAxis, axisValue);
            if (activeButtons.size() > 0) {
                for (KnightFightMappings.Button b : activeButtons) {
                    if (isDown) l.buttonDown(this, b);
                    if (!isDown) l.buttonUp(this, b);
                }
            }
        }

        return false;
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
