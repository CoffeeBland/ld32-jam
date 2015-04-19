package com.dagothig.knightfight.game;

import com.dagothig.knightfight.input.KnightFightController;
import com.dagothig.knightfight.input.KnightFightControllerListener;
import com.dagothig.knightfight.input.KnightFightMappings;

public class Player implements KnightFightControllerListener {
    private static final float MIN_AXIS = 0.25f;
    public KnightFightController controller;
    public Damsel damsel;

    public Player(KnightFightController controller) {
        (this.controller = controller).addListener(this);
        damsel = new Damsel();
    }
    public Player() {
        damsel = new Damsel();
    }

    @Override
    public boolean buttonDown(KnightFightController controller, KnightFightMappings.Button button) {
        switch (button) {
            case THROW:
                damsel.wantsToThrow = true;
                break;
            case JUMP:
                damsel.wantsToJump = true;
                break;
        }
        return false;
    }

    @Override
    public boolean buttonUp(KnightFightController controller, KnightFightMappings.Button button) {
        return false;
    }

    @Override
    public boolean axisMoved(KnightFightController controller, KnightFightMappings.Axis axis, float value) {
        switch (axis) {
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
}
