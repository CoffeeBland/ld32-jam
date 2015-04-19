package com.dagothig.knightfight.game;

public class WorldLayerFactory {

    public static WorldLayer createWorldLayer(WorldLoader.WorldManifestLayer manifest) {
        WorldLayer layer = null;

        switch (manifest.type) {
            case "centered":
                layer = new CenteredImageWorldLayer(manifest.filename);
                break;
        }

        return layer;
    }

}
