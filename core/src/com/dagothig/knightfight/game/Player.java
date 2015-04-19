package com.dagothig.knightfight.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.input.XboxMappings;

public class Player implements ControllerListener {
    private static final float MIN_AXIS = 0.1f;
    public Controller controller;
    public Damsel damsel;

    public Player(Controller controller) {
        (this.controller = controller).addListener(this);
        damsel = new Damsel();
    }

    @Override
    public void connected(Controller controller) {
        System.out.println("Player connected");
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println("Player disconnected");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        switch (XboxMappings.Axis.getAxis(axisCode)) {
            case LEFT_HORIZONTAL:
                damsel.xAxis = Math.abs(value) < MIN_AXIS ? 0 : value;
                break;
            case LEFT_VERTICAL:
                damsel.yAxis = Math.abs(value) < MIN_AXIS ? 0 : -value;
                break;
            case RIGHT_HORIZONTAL:
                break;
            case RIGHT_VERTICAL:
                break;
        }
        return false;
    }

    @Override public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
    @Override public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }
}
