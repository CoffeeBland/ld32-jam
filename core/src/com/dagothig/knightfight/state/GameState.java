package com.dagothig.knightfight.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.*;
import com.dagothig.knightfight.input.KnightFightKbdController;
import com.dagothig.knightfight.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GameState extends State<Pair<List<Player>, List<KnightFightKbdController>>> {
    List<KnightFightKbdController> keyboardControllers = new ArrayList<>();
    World world;

    public GameState() {
        super();

        setBackgroundColor(new Color(0.5f, 0.7f, 1f, 1));
    }

    @Override
    public void onTransitionInStart(boolean firstTransition, Pair<List<Player>, List<KnightFightKbdController>> payload) {
        super.onTransitionInStart(firstTransition, payload);

        this.keyboardControllers = payload.second;
        world = WorldLoader.loadMapByName("map1");
        for (Player player : payload.first) {
            player.damsel.pos.add(
                    ((float)Math.random() * 2000),
                    ((float)Math.random() * 2000),
                    0
            );
            world.addPlayer(player);
        }
        /*for (int i = 0, n = 30; i < n; i++) {
            Knight knight = new Knight();
            knight.pos.add(
                    ((float)Math.random() * 2000),
                    ((float)Math.random() * 2000),
                    0
            );
            world.add(knight);
        }*/
    }

    @Override
    public boolean shouldBeReused() {
        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        world.render(batch);
    }

    @Override
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public boolean keyUp(int keycode) {
        for (KnightFightKbdController kfc : keyboardControllers) {
            return kfc.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (KnightFightKbdController kfc : keyboardControllers) {
            return kfc.keyDown(keycode);
        }
        return false;
    }
}
