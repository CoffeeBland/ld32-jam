package com.dagothig.knightfight.game.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.Camera;
import com.dagothig.knightfight.game.Player;
import com.dagothig.knightfight.game.entity.Entity;
import com.dagothig.knightfight.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class World {
    public List<Polygon> polygons = new ArrayList<>();
    public List<WorldLayer> layers = new ArrayList<>();
    public List<Player> players = new ArrayList<>();
    public Camera camera;
    public ActorsWorldLayer entitiesLayer;

    public float airFriction = 0.98f;
    public Vector3 gravity = new Vector3(0, 0, 1.75f);

    public World() {
        this.camera = new Camera();
    }

    public void add(Entity entity) {
        entitiesLayer.entities.add(entity);
    }
    public void remove(Entity entity) {
        entitiesLayer.entities.remove(entity);
    }

    public List<WorldLayer> getLayers() {
        return layers;
    }

    public void addLayer(WorldLayer layer) {
        this.layers.add(layer);
    }

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public void update(float delta) {
        camera.update(players);

        for (WorldLayer layer : getLayers()) {
            layer.update(delta, this);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(ColorUtil.whitePixel(), 100, 100, 0, 0, 1, 1900);
        batch.draw(ColorUtil.whitePixel(), 100, 100, 0, 0, 1900, 1);
        batch.draw(ColorUtil.whitePixel(), 1800, 1800, 0, 0, 1900, 1);
        batch.draw(ColorUtil.whitePixel(), 1800, 1800, 0, 0, 1, 1900);
        batch.setProjectionMatrix(camera.getPosition());
        for (WorldLayer layer : layers) {
            layer.render(batch);
        }
    }

    public void addEmptyPlayersLayer() {
        entitiesLayer = new ActorsWorldLayer();
        layers.add(entitiesLayer);
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
