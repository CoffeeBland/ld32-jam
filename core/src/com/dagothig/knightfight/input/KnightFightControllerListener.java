package com.dagothig.knightfight.input;

public interface KnightFightControllerListener {
    boolean buttonDown(KnightFightController controller, KnightFightMappings.Button button);
    boolean buttonUp(KnightFightController controller, KnightFightMappings.Button button);
    boolean axisMoved(KnightFightController controller, KnightFightMappings.Axis axis, float value);
}
