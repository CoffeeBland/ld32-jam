package com.dagothig.knightfight.game.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.entity.Entity;

import java.util.*;

public class ActorsWorldLayer implements WorldLayer {

    public List<Entity> entities = new ArrayList<>();
    public Comparator<Entity> ySorter = new Comparator<Entity>() {
        @Override
        public int compare(Entity lhs, Entity rhs) {
            return -Float.compare(lhs.pos.y, rhs.pos.y);
        }
    };

    @Override
    public void render(SpriteBatch batch) {
        Collections.sort(entities, ySorter);
        for (Entity entity : entities) {
            entity.render(batch);
        }
    }

    @Override
    public void update(float delta, World world) {
        for (Entity entity : entities) {
            entity.update(delta, world);
        }
    }
}
