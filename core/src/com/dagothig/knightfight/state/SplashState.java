package com.dagothig.knightfight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.util.ColorUtil;
import com.dagothig.knightfight.util.FontUtil;

public class SplashState extends AutoSwitchState {
    @Override protected float getDuration() { return 2000; }
    @Override protected Class<? extends State> getNextStateClass() { return PlayerState.class; }
    @Override protected Color getTransitionColor() { return new Color(0xefe8e100); }
    @Override protected float getTransitionDuration() { return TRANSITION_MEDIUM; }

    private static String LOGO_PATH = "coffeebland.png";
    private Texture bg;
    private Texture logo;
    private BitmapFont font;

    public SplashState() {
        setBackgroundColor(Color.WHITE.cpy());
        bg = ColorUtil.whitePixel();
        font = FontUtil.normalFont();
        logo = new Texture(Gdx.files.internal(LOGO_PATH));
    }

    @Override
    public void render(SpriteBatch batch) {
        float imgX = (Gdx.graphics.getWidth() / 2) - (logo.getWidth() / 2); // Half the logo
        float imgY = (Gdx.graphics.getHeight() / 2) - (logo.getHeight() / 2); // Half the logo

        batch.setColor(new Color(0x75858AFF));
        batch.draw(bg, 12, 12, Gdx.graphics.getWidth()-24, Gdx.graphics.getHeight()-24);
        batch.setColor(Color.WHITE.cpy());
        batch.draw(logo, imgX, imgY);
        font.setColor(Color.BLACK.cpy());
        String text = "Catiniata - Dagothig - Kiasaki";
        font.draw(batch, text, (Gdx.graphics.getWidth()/2)-(font.getSpaceWidth()/2), 48);
    }
}