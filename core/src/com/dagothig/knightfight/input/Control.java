package com.dagothig.knightfight.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.*;

import static com.badlogic.gdx.Input.Buttons;
import static com.badlogic.gdx.Input.Keys;

/**
 * Created by dagothig on 8/23/14.
 */
public enum Control {
    LEFT_CLICK(Buttons.LEFT),
    RIGHT_CLICK(Buttons.RIGHT),
    MOVE_LEFT(Keys.LEFT),
    MOVE_RIGHT(Keys.RIGHT),
    MOVE_UP(Keys.UP),
    MOVE_DOWN(Keys.DOWN),
    RAISE_CELLPHONE(Keys.SPACE),
    MENU(Keys.ESCAPE),
    CANCEL(Keys.ESCAPE),
    PRESS_BUTTON(Keys.SPACE, Keys.ENTER),
    SKIP(Keys.ANY_KEY);
    /*
    MOVE_LEFT(Input.Keys.LEFT),
    MOVE_RIGHT(Input.Keys.RIGHT),
    MOVE_UP(Input.Keys.UP),
    MOVE_DOWN(Input.Keys.DOWN),
    RAISE_CELLPHONE(Input.Keys.SPACE),
    ENTER(Input.Keys.ENTER),
    EXIT_MENU(Input.Keys.ESCAPE),
    OPEN_MENU(Input.Keys.ESCAPE),
    LEFT_CLICK(Input.Buttons.LEFT),
    SKIP_INTRO1(Input.Keys.ESCAPE),
    SKIP_INTRO2(Input.Keys.SPACE),
    SKIP_INTRO3(Input.Keys.ENTER);
    */

    private static Preferences prefs;
    private static final Map<Integer, Collection<Control>> keyCodesToControls = new HashMap<Integer, Collection<Control>>();

    static {
        prefs = Gdx.app.getPreferences("Controls");

        readPrefs();
    }

    public static void readPrefs() {
        keyCodesToControls.clear();
        for (Control control : Control.values()) {
            // Get settings
            int qty = prefs.getInteger(control.getAmountKey(), 0);
            if (qty > 0) {
                int[] keyCodes = new int[qty];
                for (int i = 0; i < qty; i++) {
                    keyCodes[i] = prefs.getInteger(control.name() + "_" + qty, Keys.ENTER);
                }
                control.setKeyCodes(keyCodes);
            } else {
                control.setKeyCodes(control.getDefaultKeyCodes());
            }

            updateKeyCodesToControls(control);
        }
    }

    public static void savePrefs() {
        keyCodesToControls.clear();
        for (Control control : Control.values()) {
            // Clear previous keycodes
            int qty = prefs.getInteger(control.getAmountKey(), -1);
            if (qty  >= 0) {
                prefs.remove(control.getAmountKey());
                for (int i = 0; i < qty; i++) {
                    prefs.remove(control.name() + "_" + i);
                }
            }

            // Save new ones
            if (control.getKeyCodes() != control.getDefaultKeyCodes()) {
                qty = control.getKeyCodes().size();
                prefs.putInteger(control.getAmountKey(), qty);
                for (int i = 0; i < qty; i++) {
                    prefs.putInteger(control.name() + "_" + i, control.getKeyCodes().get(i));
                }
            }

            updateKeyCodesToControls(control);
        }
        prefs.flush();
    }

    private static void updateKeyCodesToControls(Control control) {
        for (int keyCode : control.getKeyCodes()) {
            Collection<Control> controls;

            if (!keyCodesToControls.containsKey(keyCode)) {
                controls = new ArrayList<Control>();
                keyCodesToControls.put(keyCode, controls);
            } else {
                controls = keyCodesToControls.get(keyCode);
            }

            controls.add(control);
        }
    }

    public static Collection<Control> getControls(int keycode) {
        return keyCodesToControls.get(keycode);
    }

    private Control(int... keyCodes) {
        defaultKeyCodes = new ArrayList<Integer>(keyCodes.length);
        for (int i : keyCodes) {
            defaultKeyCodes.add(i);
        }
        this.keyCodes = new ArrayList<Integer>();
    }
    private final List<Integer> keyCodes, defaultKeyCodes;
    public List<Integer> getKeyCodes() {
        return keyCodes;
    }
    public List<Integer> getDefaultKeyCodes() {
        return defaultKeyCodes;
    }

    protected String getAmountKey() {
        return name() + "_AMOUNT";
    }

    public void setKeyCodes(int... keyCodes) {
        this.keyCodes.clear();
        for (int i : keyCodes) {
            this.keyCodes.add(i);
        }
    }
    public void setKeyCodes(List<Integer> keyCodes) {
        this.keyCodes.clear();
        this.keyCodes.addAll(keyCodes);
    }
    public void addKeyCode(int keyCode) {
        keyCodes.add(keyCode);
    }
    public void removeKeyCode(int keyCode) {
        keyCodes.remove(keyCode);
    }
}
