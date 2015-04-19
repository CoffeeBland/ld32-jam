package com.dagothig.knightfight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.util.ColorUtil;
import com.dagothig.knightfight.util.FontUtil;

import static com.dagothig.knightfight.util.Dimensions.PADDING;

public class SplashState extends AutoSwitchState {
    @Override protected float getDuration() { return 1200; }
    @Override protected Class<? extends State> getNextStateClass() { return PlayerState.class; }
    @Override protected Color getTransitionColor() { return new Color(0xFFFFFFFF); }
    @Override protected float getTransitionDuration() { return TRANSITION_MEDIUM; }

    private static String LOGO_PATH = "coffeebland.png";
    private Texture bg;
    private Texture logo;
    private BitmapFont font;
    private GlyphLayout text;

    public SplashState() {
        setBackgroundColor(Color.LIGHT_GRAY.cpy());
        bg = ColorUtil.whitePixel();
        logo = new Texture(Gdx.files.internal(LOGO_PATH));

        font = FontUtil.pixel();
        font.setColor(Color.BLACK.cpy());
        text = new GlyphLayout(font, "Catiniata - Dagothig - Kiasaki");
    }

    @Override
    public void render(SpriteBatch batch) {
        float imgX = (Gdx.graphics.getWidth() / 2) - (logo.getWidth() / 2);
        float imgY = (Gdx.graphics.getHeight() / 2) - (logo.getHeight() / 2);

        batch.setColor(new Color(0x75858AFF));
        batch.draw(bg, PADDING, PADDING, Gdx.graphics.getWidth()- PADDING * 2, Gdx.graphics.getHeight()- PADDING * 2);
        batch.setColor(Color.WHITE.cpy());
        batch.draw(logo, imgX, imgY);
        font.draw(batch, text, (Gdx.graphics.getWidth()/2)-(text.width/2), PADDING * 4);
    }
}