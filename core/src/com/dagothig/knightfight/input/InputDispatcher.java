package com.dagothig.knightfight.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.*;

/**
 * Created by dagothig on 8/23/14.
 */
public class InputDispatcher implements InputProcessor {
    private final Map<Control, OnKeyListener> keyListeners = new HashMap<>();
    private final Set<Clickable> clickables = new TreeSet<>(new Comparator<Clickable>() {
        @Override
        public int compare(Clickable clickable, Clickable clickable2) {
            return Float.compare(clickable.getClickPriority(), clickable2.getClickPriority());
        }
    });
    private final Collection<Clickable> clickablesToRemove = new ArrayList<>();
    private final List<Integer> pressedButtons = new ArrayList<>();
    private Clickable downClickable;

    @Override
    public boolean keyDown(int keycode) {
        Collection<Control> controls = Control.getControls(keycode);

        boolean returnValue = false;
        if (controls != null) {
            for (Control control : controls) {
                if (keyListeners.containsKey(control)) {
                    keyListeners.get(control).onKeyDown(control);
                    returnValue = true;
                }
            }
        }

        return returnValue;
    }

    @Override
    public boolean keyUp(int keycode) {
        Collection<Control> controls = Control.getControls(keycode);

        boolean returnValue = false;
        if (controls != null) {
            for (Control control : controls) {
                if (keyListeners.containsKey(control)) {
                    keyListeners.get(control).onKeyUp(control);
                    returnValue = true;
                }
            }
        }

        return returnValue;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pressedButtons.add(button);
        int y = Gdx.graphics.getHeight() - screenY;
        boolean returnValue = false;
        downClickable = null;

        for (Clickable clickable : clickables) {
             if (clickable.onMouseDown(button, screenX, y)) {
                 returnValue = true;
                 downClickable = clickable;
                 break;
             }
        }

        return returnValue;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        pressedButtons.remove(button);
        int y = Gdx.graphics.getHeight() - screenY;
        boolean returnValue = false;

        if (downClickable != null && downClickable.onMouseUp(button, screenX, y)) {
            returnValue = true;
        }
        downClickable = null;

        return returnValue;
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

    public void update(float delta) {
        if (this != Gdx.input.getInputProcessor())
            return;

        Input input = Gdx.app.getInput();

        // Notify key listeners
        Collection<Map.Entry<Control, OnKeyListener>> keysToDown = new ArrayList<Map.Entry<Control, OnKeyListener>>();
        controls: for (Map.Entry<Control, OnKeyListener> entry : keyListeners.entrySet()) {
            for (int keyCode : entry.getKey().getKeyCodes()) {
                if (input.isKeyPressed(keyCode)) {
                    keysToDown.add(entry);
                    continue controls;
                }
            }
        }
        for (Map.Entry<Control, OnKeyListener> entry : keysToDown) {
            entry.getValue().onKeyIsDown(entry.getKey());
        }

        // === Mouse === //
        // Clean clickables to remove
        for (Clickable clickable: clickablesToRemove) {
            clickables.remove(clickable);
        }
        clickablesToRemove.clear();

        // Notify clickables
        for (int btn : pressedButtons) {
            if (!input.isButtonPressed(btn)) {
                pressedButtons.remove(btn);
            }
        }
        int x = input.getX(), y = Gdx.graphics.getHeight() - input.getY();
        if (downClickable != null) {
            downClickable.update(delta);
            downClickable.onMouseMove(x, y);
        } else {
            boolean hasFoundMove = false;
            for (Clickable clickable : clickables) {
                clickable.update(delta);
                if (!hasFoundMove) {
                    hasFoundMove = clickable.onMouseMove(x, y);
                }
            }
        }

        // Clean clickables to remove (again)
        for (Clickable clickable: clickablesToRemove) {
            clickables.remove(clickable);
        }
        clickablesToRemove.clear();
    }

    public void listenTo(OnKeyListener listener, Control... controls) {
        for (Control control : controls) {
            keyListeners.put(control, listener);
        }
    }
    public void register(Clickable clickable) {
        clickables.add(clickable);
    }

    public static interface OnKeyListener {
        public void onKeyDown(Control control);
        public void onKeyUp(Control control);
        public void onKeyIsDown(Control control);
    }
}
