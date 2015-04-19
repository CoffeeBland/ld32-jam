package com.dagothig.knightfight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

import java.util.List;

public class Camera {
    public static final int WORLD_WIDTH = 2000;
    public static final int WORLD_HEIGHT = 2000;

    protected OrthographicCamera innerCamera = new OrthographicCamera();

    public void update(List<Player> players) {
        int offsetMaxX = WORLD_WIDTH - Gdx.graphics.getWidth();
        int offsetMaxY = WORLD_HEIGHT - Gdx.graphics.getHeight();

        int offsetMinX = 0;
        int offsetMinY = 0;

        // Used to center camera
        int sumOfPlayerXs = 0;
        int sumOfPlayerYs = 0;

        // Used to scale/zoom camera
        int playersMinX = Math.round(players.get(0).damsel.pos.x);
        int playersMaxX = Math.round(players.get(0).damsel.pos.x);
        int playersMinY = Math.round(players.get(0).damsel.pos.y);
        int playersMaxY = Math.round(players.get(0).damsel.pos.y);

        for (Player p : players) {
            sumOfPlayerXs += p.damsel.pos.x;
            sumOfPlayerYs += p.damsel.pos.y;
            if (p.damsel.pos.x < playersMinX) playersMinX = Math.round(p.damsel.pos.x);
            if (p.damsel.pos.x > playersMaxX) playersMaxX = Math.round(p.damsel.pos.x);
            if (p.damsel.pos.y < playersMinY) playersMinY = Math.round(p.damsel.pos.y);
            if (p.damsel.pos.y > playersMaxY) playersMaxY = Math.round(p.damsel.pos.y);
        }

        int camX = (sumOfPlayerXs/players.size()) - (Gdx.graphics.getWidth()/2);
        int camY = (sumOfPlayerYs/players.size()) - (Gdx.graphics.getHeight()/2);

        if (camX > offsetMaxX) camX = offsetMaxX;
        if (camX < offsetMinX) camX = offsetMinX;
        if (camY > offsetMaxY) camY = offsetMaxY;
        if (camY < offsetMinY) camY = offsetMinY;

        innerCamera.setToOrtho(false, playersMaxX - playersMinX + 1200, playersMaxY - playersMinY + 1200);

        innerCamera.translate(
            camX-innerCamera.position.x * 0.2f,
            camY-innerCamera.position.y * 0.2f
        );
        innerCamera.update();
    }

    public Matrix4 getPosition() {
        return this.innerCamera.combined;
    }
}
