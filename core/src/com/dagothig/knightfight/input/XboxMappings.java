package com.dagothig.knightfight.input;

/**
 * Created by dagothig on 4/18/15.
 */
public class XboxMappings {
    public static enum Button {
        A(0),
        B(1),
        X(2),
        Y(3),
        LEFT_SHOULDER(4),
        RIGHT_SHOULDER(5),
        SELECT(6),
        START(7),
        HOME(8),
        LEFT_STICK(9),
        RIGHT_STICK(10);

        public final int buttonIndex;

        private Button(int buttonIndex) {
            this.buttonIndex = buttonIndex;
        }

        public static Button getButton(int buttonIndex) {
            // TODO don't assume the values() are in the same order as the index (they are for now, but that could change)
            return values()[buttonIndex];
        }
    }

    public static enum Axis {
        LEFT_HORIZONTAL(0),
        LEFT_VERTICAL(1),
        LEFT_TRIGGER(2),
        RIGHT_HORIZONTAL(3),
        RIGHT_VERTICAL(4),
        RIGHT_TRIGGER(5);

        public final int axisIndex;

        private Axis(int axisIndex) {
            this.axisIndex = axisIndex;
        }

        public static Axis getAxis(int axisIndex) {
            // TODO don't assume the values() are in the same order as the index (they are for now, but that could change)
            return values()[axisIndex];
        }
    }
}
