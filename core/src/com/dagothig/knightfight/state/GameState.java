package com.dagothig.knightfight.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.Player;
import com.dagothig.knightfight.game.World;
import com.dagothig.knightfight.game.WorldLoader;

import java.util.List;

public class GameState extends State<List<Player>> {

    List<Player> players;
    World world;

    public GameState() {
        super();

        setBackgroundColor(new Color(0.5f, 0.7f, 1f, 1));
    }

    @Override
    public void onTransitionInStart(boolean firstTransition, List<Player> players) {
        super.onTransitionInStart(firstTransition, players);

        this.players = players;
        world = WorldLoader.loadEmptyMap();
        for (Player player : players) {
            player.damsel.pos.add(400 * (float)Math.random(), 400 * (float)Math.random(), 0);
            world.add(player.damsel);
        }
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
}
