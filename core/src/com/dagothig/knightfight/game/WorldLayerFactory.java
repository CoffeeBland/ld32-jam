package com.dagothig.knightfight.game;

public class WorldLayerFactory {

    public static WorldLayer createWorldLayer(String filename, String type) {
        WorldLayer layer = null;

        switch (type) {
            case "centered":
                layer = new CenteredImageWorldLayer(filename);
                break;
        }

        return layer;
    }

}
