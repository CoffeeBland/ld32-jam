package com.dagothig.knightfight.game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class WorldLoader {
   protected static ObjectMapper mapper;

   public static World loadMapByName(String name) {
      if (mapper == null) mapper = new ObjectMapper();

      World world = new World();

      File worldManifestFile = new File("maps/" + name + ".json");
      WorldManifest manifest;
      try {
         manifest = mapper.readValue(worldManifestFile, WorldManifest.class);
      } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException("Couldn't load map by name: " + name);
      }

      for (WorldManifestLayer l : manifest.backgrounds) {
         world.addLayer(WorldLayerFactory.createWorldLayer(l));
      }
      world.addEmptyPlayersLayer();
      for (WorldManifestLayer l : manifest.foregrounds) {
         world.addLayer(WorldLayerFactory.createWorldLayer(l));
      }

      for (WorldManifestPolygon p : manifest.polygons) {
         world.addPolygon(new WorldPolygon(p.points, p.deadly, p.exclusive));
      }

      return world;
   }
    public static World loadEmptyMap() {
        World world = new World();
        world.addEmptyPlayersLayer();
        return world;
    }

   public static class WorldManifest {
      public WorldManifestLayer[] backgrounds;
      public WorldManifestLayer[] foregrounds;

      public WorldManifestPolygon[] polygons;
   }

   public static class WorldManifestLayer {
      public String filename;
      public String type;
   }

   public static class WorldManifestPolygon {
      public int[][] points;
      public boolean deadly;
      public boolean exclusive;
   }
}
