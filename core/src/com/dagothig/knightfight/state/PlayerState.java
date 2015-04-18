package com.dagothig.knightfight.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.Player;
import com.dagothig.knightfight.input.XboxMappings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagothig on 4/18/15.
 */
public class PlayerState extends State<Void> {

    public List<Player> players = new ArrayList<>();
    public List<Controller> controllers = new ArrayList<>();
    public BitmapFont font;

    @Override
    public void onTransitionInStart(boolean firstTransition, Void aVoid) {
        super.onTransitionInStart(firstTransition, aVoid);

        for (Controller controller: Controllers.getControllers()) {
            controllers.add(controller);
            controller.addListener(new ControllerAdapter() {
                @Override
                public boolean buttonDown(Controller controller, int buttonIndex) {
                    switch (XboxMappings.Button.getButton(buttonIndex)) {
                        case START:
                            // Require at least two player before being available
                            if (readyForPlayer()) break;
                            for (Player player : players) player.controller.removeListener(this);
                            switchToState(GameState.class, Color.WHITE, TRANSITION_MEDIUM, players);
                            break;
                        case A:
                            players.add(new Player(controller));
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public boolean readyForPlayer() {
        return players.size() > 2;
    }

    @Override
    public boolean shouldBeReused() {
        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        //font.draw(batch, players.size());
    }
    @Override
    public void update(float delta) {

    }
}
