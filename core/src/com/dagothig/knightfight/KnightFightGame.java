package com.dagothig.knightfight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.state.SplashState;
import com.dagothig.knightfight.state.StateManager;

public class KnightFightGame extends ApplicationAdapter {
    StateManager mgr;
	SpriteBatch batch;
    long nanoTime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        batch.setBlendFunction(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        batch.enableBlending();
        mgr = new StateManager(SplashState.class);
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        batch = new SpriteBatch();
        batch.setBlendFunction(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        batch.enableBlending();
    }

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        long current = System.nanoTime();
        if (nanoTime != 0) {
            // We pass the delta in milliseconds rather than nanoseconds
            float delta = (current - nanoTime) / 1000000f;
            mgr.update(delta);
            batch.begin();
            mgr.render(batch);
            batch.end();
        }
        nanoTime = current;
	}
}
