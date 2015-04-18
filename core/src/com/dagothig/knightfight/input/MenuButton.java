package com.dagothig.knightfight.input;


/**
 * Created by dagothig on 11/2/14.
 */
public abstract class MenuButton extends Button  {
    boolean hasFocus, isPressed;
    MenuButton toTop = this;
    MenuButton toLeft = this;
    MenuButton toRight = this;
    MenuButton toBottom = this;

    public MenuButton getToTop() {
        return toTop;
    }
    public void setToTop(MenuButton toTop) {
        this.toTop = toTop;
        toTop.toBottom = this;
    }

    public MenuButton getToLeft() {
        return toLeft;
    }
    public void setToLeft(MenuButton toLeft) {
        this.toLeft = toLeft;
        toLeft.toRight = this;
    }

    public MenuButton getToRight() {
        return toRight;
    }
    public void setToRight(MenuButton toRight) {
        this.toRight = toRight;
        toRight.toLeft = this;
    }

    public MenuButton getToBottom() {
        return toBottom;
    }
    public void setToBottom(MenuButton toBottom) {
        this.toBottom = toBottom;
        toBottom.toTop = this;
    }

    public State getState() {
        State returnState = super.getState();
        if (isPressed) {
            returnState = returnState.getPrioritisedState(State.DOWN);
        } else if (hasFocus) {
            returnState = returnState.getPrioritisedState(State.HOVERED);
        }
        return returnState;
    }

    @Override
    public boolean isButtonValid(int button) {
        return Control.LEFT_CLICK.getKeyCodes().contains(button);
    }
}
