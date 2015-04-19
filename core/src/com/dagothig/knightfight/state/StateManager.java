package com.dagothig.knightfight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.res.Textures;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dagothig on 8/23/14.
 */
@SuppressWarnings("unchecked")
public class StateManager {
    public StateManager(Class<? extends State> initialState) {
        switchToState(initialState, Color.BLACK.cpy(), State.TRANSITION_LONG);
    }

    private Map<String, State> states = new HashMap<String, State>();
    private State currentState, nextState;
    private Object switchArgs;
    private Color transitionColor;
    private float remainingTransitionTime, transitionLength;
    private boolean hasSwitched = false, isNewState = false;

    public Color getBackgroundColor() {
        if (currentState != null)
            return currentState.getBackgroundColor();
        else
            return Color.BLACK.cpy();
    }

    public void switchToState(Class<? extends State> stateType, Color transitionColor, float transitionLength, Object switchArgs) {
        if (stateType == null) {
            return;
        }

        if (transitionColor == null) {
            transitionColor = Color.BLACK.cpy();
        }

        // Get state
        if (states.containsKey(stateType.getName())) {
            nextState = states.get(stateType.getName());
            isNewState = false;
        } else {
            try {
                nextState = stateType.newInstance();
                nextState.setStateManager(this);
                if (nextState.shouldBeReused()) {
                    states.put(stateType.getName(), nextState);
                }
                isNewState = true;
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("The state could not be instantiated");
            }
        }
        // Set the transition fields
        this.transitionColor = transitionColor;
        remainingTransitionTime = transitionLength;
        this.transitionLength = transitionLength;
        this.switchArgs = switchArgs;

        // Notify state ( if there is none then we will directly animate the fade in)
        if (currentState != null) {
            currentState.onTransitionOutStart();
        } else {
            remainingTransitionTime /= 2;
        }
        hasSwitched = false;
        Gdx.input.setInputProcessor(null);
    }
    public void switchToState(Class<? extends State> stateType, Color transitionColor, float transitionLength) {
        switchToState(stateType, transitionColor.cpy(), transitionLength, null);
    }

    private void renderTransition(SpriteBatch batch) {
        if (remainingTransitionTime > 0) {
            float halfLength = (transitionLength / 2f);
            transitionColor.a = 1 - (Math.abs(remainingTransitionTime - halfLength) / halfLength);
            batch.setColor(transitionColor);
            batch.draw(Textures.WHITE_PIXEL, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(Color.WHITE);
        }
    }
    public void render(SpriteBatch batch) {
        if (currentState != null) {
            currentState.render(batch);
        }
        renderTransition(batch);
    }

    private void updateTransition(float delta) {
        if (remainingTransitionTime > 0) {
            remainingTransitionTime -= delta;

            if (remainingTransitionTime <= 0 && currentState != null) {
                currentState.onTransitionInFinish();
            }

            if (remainingTransitionTime <= transitionLength / 2 && !hasSwitched) {
                if (currentState != null) {
                    currentState.onTransitionOutFinish();
                }
                if (nextState != null) {
                    nextState.onTransitionInStart(isNewState, switchArgs);
                    Gdx.input.setInputProcessor(nextState);
                }
                currentState = nextState;
                hasSwitched = true;
            }
        }
    }
    public void update(float delta) {
        updateTransition(delta);
        if (currentState != null) {
            currentState.update(delta);
        }
    }
}
