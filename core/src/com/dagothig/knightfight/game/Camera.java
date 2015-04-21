package com.dagothig.knightfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

import java.util.List;

public class Camera {
    public static final int WORLD_WIDTH = 2000;
    public static final int WORLD_HEIGHT = 2000;

    protected OrthographicCamera innerCamera = new OrthographicCamera();

    protected float lastRatio, lastX, lastY;

    public void update(List<Player> players) {
        // TODO: clamp to map

        // Used to scale/zoom camera
        float playersMinX = Integer.MAX_VALUE;
        float playersMaxX = Integer.MIN_VALUE;
        float playersMinY = Integer.MAX_VALUE;
        float playersMaxY = Integer.MIN_VALUE;

        for (Player p : players) {
            playersMinX = Math.min(p.damsel.pos.x, playersMinX);
            playersMaxX = Math.max(p.damsel.pos.x, playersMaxX);
            playersMinY = Math.min(p.damsel.pos.y, playersMinY);
            playersMaxY = Math.max(p.damsel.pos.y + p.damsel.pos.z, playersMaxY);
        }

        // Offset to account for the player size (we want the players to be fully encompassed by the camera)
        playersMinX -= players.get(0).damsel.getVisualWidth() / 2;
        playersMaxX += players.get(0).damsel.getVisualWidth() / 2;
        playersMinY -= players.get(0).damsel.shadow.getHeight();
        playersMaxY += players.get(0).damsel.getVisualHeight() + players.get(0).damsel.shadow.getHeight() / 2;

        float deltaX = playersMaxX - playersMinX;
        float deltaY = playersMaxY - playersMinY;

        float ratio = Math.max(Math.max(deltaX / (float)Gdx.graphics.getWidth(), deltaY / (float)Gdx.graphics.getHeight()), 1);
        lastRatio = lastRatio * 0.9f + ratio * 0.1f;
        lastX = lastX * 0.9f + (playersMinX + deltaX / 2) * 0.1f;
        lastY = lastY * 0.9f + (playersMinY + deltaY / 2) * 0.1f;
        ratio = lastRatio;

        float transformedWidth = Gdx.graphics.getWidth() * ratio;
        float transformedHeight = Gdx.graphics.getHeight() * ratio;

        innerCamera.setToOrtho(false,
                transformedWidth,
                transformedHeight
        );
        innerCamera.translate(
                Math.round(lastX - transformedWidth / 2),
                Math.round(lastY - transformedHeight / 2)
        );

        innerCamera.update();
    }

    public Matrix4 getPosition() {
        return this.innerCamera.combined;
    }
}
