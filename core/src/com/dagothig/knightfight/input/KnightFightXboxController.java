package com.dagothig.knightfight.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class KnightFightXboxController extends KnightFightController implements ControllerListener {
    protected Controller controller;

    public KnightFightXboxController(Controller controller) {
        (this.controller = controller).addListener(this);
    }

    @Override public void connected(Controller controller) {}
    @Override public void disconnected(Controller controller) {}

    protected boolean handleButton(Controller controller, int buttonCode, boolean isDown) {
        List<KnightFightMappings.Button> activeButtons = new ArrayList<>();

        switch (XboxMappings.Button.getButton(buttonCode)) {
            case START:
                activeButtons.add(KnightFightMappings.Button.START);
                break;
            case A:
                activeButtons.add(KnightFightMappings.Button.JOIN);
                activeButtons.add(KnightFightMappings.Button.JUMP);
                break;
        }

        if (activeButtons.size() > 0) {
            for (KnightFightControllerListener l : this.listeners) {
                for (KnightFightMappings.Button b : activeButtons) {
                    if (isDown) l.buttonDown(this, b);
                    if (!isDown) l.buttonUp(this, b);
                }
            }
        }

        return false;
    }

    @Override public boolean buttonDown(Controller controller, int buttonCode) {
        return handleButton(controller, buttonCode, true);
    }

    @Override public boolean buttonUp(Controller controller, int buttonCode) {
        return handleButton(controller, buttonCode, false);
    }

    @Override public boolean axisMoved(Controller controller, int axisCode, float value) {
        KnightFightMappings.Axis axis = null;

        switch (XboxMappings.Axis.getAxis(axisCode)) {
            case LEFT_HORIZONTAL:
                axis = KnightFightMappings.Axis.LEFT_HORIZONTAL;
            case LEFT_VERTICAL:
                axis = KnightFightMappings.Axis.LEFT_VERTICAL;
            case LEFT_TRIGGER:
                axis = KnightFightMappings.Axis.LEFT_TRIGGER;
            case RIGHT_HORIZONTAL:
                axis = KnightFightMappings.Axis.RIGHT_HORIZONTAL;
            case RIGHT_VERTICAL:
                axis = KnightFightMappings.Axis.RIGHT_VERTICAL;
            case RIGHT_TRIGGER:
                axis = KnightFightMappings.Axis.RIGHT_TRIGGER;
        }

        if (axis != null) {
            for (KnightFightControllerListener l : this.listeners) {
                l.axisMoved(this, axis, value);
            }
        }

        return false;
    }

    @Override public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
    @Override public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }
}
