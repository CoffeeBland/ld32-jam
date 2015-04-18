package com.dagothig.knightfight.state;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.Player;

import java.util.List;

/**
 * Created by dagothig on 4/18/15.
 */
public class GameState extends State<List<Player>> {

    @Override
    public boolean shouldBeReused() {
        return false;
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void update(float delta) {
    }
}
