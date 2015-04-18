package com.dagothig.knightfight.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by dagothig on 11/2/14.
 */
public class Menu implements InputDispatcher.OnKeyListener {
    private WeakReference<InputDispatcher> registered;
    MenuButton currentButton, cancelButton;

    SortedSet<MenuButton> buttons = new TreeSet<MenuButton>(new Comparator<MenuButton>() {
        @Override
        public int compare(MenuButton menuButton, MenuButton menuButton2) {
            return Float.compare(menuButton.getRenderPriority(), menuButton2.getRenderPriority());
        }
    });

    public void addButton(MenuButton button) {
        buttons.add(button);

        InputDispatcher dispatcher = registered != null ? registered.get() : null;
        if (dispatcher != null) {
            dispatcher.register(button);
        }
    }
    public void setCancelButton(MenuButton button) {
        cancelButton = button;
    }

    public void register(InputDispatcher dispatcher) {
        registered = new WeakReference<>(dispatcher);

        for (MenuButton btn : buttons) {
            dispatcher.register(btn);
        }

        dispatcher.listenTo(this,
                Control.MOVE_LEFT,
                Control.MOVE_UP,
                Control.MOVE_RIGHT,
                Control.MOVE_DOWN,
                Control.PRESS_BUTTON,
                Control.CANCEL
        );
    }

    @Override
    public void onKeyDown(Control control) {
        if (currentButton == null) {
            if (control != Control.PRESS_BUTTON) {
                currentButton = buttons.first();
                currentButton.hasFocus = true;
            }

            return;
        }

        if (currentButton.isPressed) {
            return;
        }

        switch (control) {
            case MOVE_LEFT:
                currentButton.hasFocus = false;
                currentButton = currentButton.toLeft;
                currentButton.hasFocus = true;
                break;
            case MOVE_RIGHT:
                currentButton.hasFocus = false;
                currentButton = currentButton.toRight;
                currentButton.hasFocus = true;
                break;
            case MOVE_UP:
                currentButton.hasFocus = false;
                currentButton = currentButton.toTop;
                currentButton.hasFocus = true;
                break;
            case MOVE_DOWN:
                currentButton.hasFocus = false;
                currentButton = currentButton.toBottom;
                currentButton.hasFocus = true;
                break;
            case PRESS_BUTTON:
                currentButton.onTriggered();
                break;
            case CANCEL:
                if (cancelButton != null) {
                    cancelButton.onTriggered();
                }
                break;
        }
    }

    @Override
    public void onKeyUp(Control control) {
    }

    @Override
    public void onKeyIsDown(Control control) {

    }

    public void render(SpriteBatch batch) {
        for (Button btn : buttons) {
            btn.render(batch);
        }
    }
    public void update(float delta) {}
}
