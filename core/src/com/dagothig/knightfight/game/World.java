package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class World {
    protected List<WorldPolygon> polygons = new ArrayList<>();
    protected List<WorldLayer> layers = new ArrayList<>();
    protected List<Player> players = new ArrayList<>();
    protected Camera camera;
    protected ActorsWorldLayer actorsLayer;

    public float airFriction = 0.98f;
    public float groundFriction = 0.8f;
    public float gravity = 1.75f;

    public World() {
        this.camera = new Camera();
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
        camera.update(players);

        for (WorldLayer layer : getLayers()) {
            layer.update(delta, this);
        }
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.getPosition());
        for (WorldLayer layer : layers) {
            layer.render(batch);
        }
    }

    public void addEmptyPlayersLayer() {
        actorsLayer = new ActorsWorldLayer();
        layers.add(actorsLayer);
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        this.add(player.damsel);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
