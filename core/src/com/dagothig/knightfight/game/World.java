package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    protected List<WorldPolygon> polygons = new ArrayList<>();
    protected List<WorldLayer> layers = new ArrayList<>();
    protected PlayersWorldLayer playersLayer;

    public World() {
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
    }

    public void render(SpriteBatch batch) {

    }

    public void addEmptyPlayersLayer() {
        playersLayer = new PlayersWorldLayer();
        layers.add(playersLayer);
    }
}
