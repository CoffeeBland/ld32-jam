package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.*;

public class ActorsWorldLayer implements WorldLayer {

    public List<Actor> actors = new ArrayList<>();
    public Comparator<Actor> ySorter = new Comparator<Actor>() {
        @Override
        public int compare(Actor lhs, Actor rhs) {
            return -Float.compare(lhs.pos.y, rhs.pos.y);
        }
    };

    @Override
    public void render(SpriteBatch batch) {
        Collections.sort(actors, ySorter);
        for (Actor actor : actors) {
            actor.render(batch);
        }
    }

    @Override
    public void update(float delta, World world) {
        for (Actor actor : actors) {
            actor.update(delta, world);
        }
    }
}
