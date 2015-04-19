package com.dagothig.knightfight.game;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class WorldLoader {
   private static ObjectMapper mapper;

   public static World loadMapByName(String name) {
      if (mapper == null) mapper = new ObjectMapper();

      World world = new World();

      File worldManifestFile = new File("worlds/" + name + ".json");
      WorldManifest manifest;
      try {
         manifest = mapper.readValue(worldManifestFile, WorldManifest.class);
      } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException("Couldn't load map by name: " + name);
      }

      for (WorldManifestLayer l : manifest.backgrounds) {
         world.addLayer(WorldLayerFactory.createWorldLayer(l.filename, l.type));
      }
      world.addEmptyPlayersLayer();
      for (WorldManifestLayer l : manifest.foregrounds) {
         world.addLayer(WorldLayerFactory.createWorldLayer(l.filename, l.type));
      }

      return world;
   }

   private static class WorldManifest {
      public WorldManifestLayer[] backgrounds;
      public WorldManifestLayer[] foregrounds;
   }

   private static class WorldManifestLayer {
      public String filename;
      public String type;
   }
}
