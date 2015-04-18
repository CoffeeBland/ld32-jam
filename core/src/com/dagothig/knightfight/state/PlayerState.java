package com.dagothig.knightfight.state;

import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dagothig on 4/18/15.
 */
public class PlayerState extends State<Void> implements ControllerListener {

    public List<Player> players = new ArrayList<>();

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
    public boolean shouldBeReused() {
        return false;
    }

    Set<Controller> controllahs = new HashSet<>();

    @Override public void render(SpriteBatch batch) { }
    @Override public void update(float delta) {
        for (Controller controller : Controllers.getControllers()) {
            if (controllahs.add(controller)) {
                controller.addListener(this);
            }
        }
    }
    @Override public void connected(Controller controller) {
        System.out.println("Connected");
    }
    @Override public void disconnected(Controller controller) {
        System.out.println("Disconnected");
    }
    @Override public boolean buttonDown(Controller controller, int buttonCode) { return false; }
    @Override public boolean buttonUp(Controller controller, int buttonCode) { return false; }
    @Override public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
    @Override public boolean povMoved(Controller controller, int povCode, PovDirection value) { return false; }
    @Override public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false; }
    @Override public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }
}
