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
        innerCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        int offsetMinX = 0;
        int offsetMinY = 0;

        int sumOfPlayerXs = 0;
        int sumOfPlayerYs = 0;

        for (Player p : players) {
            sumOfPlayerXs += p.damsel.pos.x;
            sumOfPlayerYs += p.damsel.pos.y;
        }

        int camX = (sumOfPlayerXs/players.size()) - (Gdx.graphics.getWidth()/2);
        int camY = (sumOfPlayerYs/players.size()) - (Gdx.graphics.getHeight()/2);

        if (camX > offsetMaxX) camX = offsetMaxX;
        if (camX < offsetMinX) camX = offsetMinX;
        if (camY > offsetMaxY) camY = offsetMaxY;
        if (camY < offsetMinY) camY = offsetMinY;

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
