package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class World {
    protected List<WorldPolygon> polygons = new ArrayList<>();
    protected List<WorldLayer> layers = new ArrayList<>();
    protected ActorsWorldLayer actorsLayer;

    public float airFriction = 0.98f;
    public float groundFriction = 0.9f;
    public float gravity = 0.75f;

    public World() {
    }

    public void add(Actor actor) {
        actorsLayer.actors.add(actor);
    }
    public void remove(Actor actor) {
        actorsLayer.actors.remove(actor);
    }

    public List<WorldLayer> getLayers() {
        return layers;
    }

    public void addLayer(WorldLayer layer) {
        this.layers.add(layer);
    }

    public void addPolygon(WorldPolygon polygon) {
        this.polygons.add(polygon);
    }

    public void update(float delta) {
        for (WorldLayer layer : layers) {
            layer.update(delta, this);
        }
    }

    public void render(SpriteBatch batch) {
        for (WorldLayer layer : layers) {
            layer.render(batch);
        }
    }

    public void addEmptyPlayersLayer() {
        actorsLayer = new ActorsWorldLayer();
        layers.add(actorsLayer);
    }
}
