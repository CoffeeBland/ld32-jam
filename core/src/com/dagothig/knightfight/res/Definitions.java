package com.dagothig.knightfight.res;

/**
 * Created by dagothig on 11/9/14.
 */
public class Definitions {
    private Definitions() {}
    public static final int
            FRAME_WIDTH = 32,
            FRAME_HEIGHT = 64,
            FEET_DECAL = -4,
            FRAME_DECAL_X = -FRAME_WIDTH / 2,
            FRAME_STAND = 0,
            FRAME_RUN = 1,
            FRAME_RUN_CELL = 2,
            FRAME_WALK = 3,
            FRAME_WALK_CELL = 4,
            CYCLE_FRAMERATE = 20;

    public static ImageSheet.Definition
            DEFAULT_BUTTON = new ImageSheet.Definition("btn_test.png", 64, 32),
            SHEET_TEST = new ImageSheet.Definition("roi.png", 24, 48),
            UI_ICONS = new ImageSheet.Definition("sprites/ui_icons.png", 32, 32),
            CELLPHONE = new ImageSheet.Definition("sprites/character/cell.png", FRAME_WIDTH, FRAME_HEIGHT);
}
