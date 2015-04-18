package com.dagothig.knightfight.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import sun.rmi.runtime.Log;

/**
 * Created by dagothig on 4/18/15.
 */
public class GameState extends State<Void> implements ControllerListener {
    @Override
    public boolean shouldBeReused() {
        return false;
    }

    @Override
    public void onTransitionInStart(boolean firstTransition, Void aVoid) {
        super.onTransitionInStart(firstTransition, aVoid);
        Controllers.addListener(this);
    }

    @Override
    public void onTransitionOutStart() {
        super.onTransitionOutStart();
        Controllers.removeListener(this);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void connected(Controller controller) {
        Log.i("")
    }

    @Override
    public void disconnected(Controller controller) {

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
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
