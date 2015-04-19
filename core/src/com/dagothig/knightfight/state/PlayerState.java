package com.dagothig.knightfight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.game.Player;
import com.dagothig.knightfight.input.XboxMappings;
import com.dagothig.knightfight.res.Textures;
import com.dagothig.knightfight.util.FontUtil;
import com.dagothig.knightfight.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.dagothig.knightfight.util.Dimensions.MIN_LADY_CELL_IMG_WIDTH;
import static com.dagothig.knightfight.util.Dimensions.PADDING;

/**
 * Created by dagothig on 4/18/15.
 */
public class PlayerState extends State<Void> {

    public List<Player> players = new ArrayList<>();
    public List<Pair<? extends Controller, PlayerControllerListener>> controllerListenerPairs = new ArrayList<>();
    public BitmapFont
            titleFont,
            infoFont,
            ladiesFont;

    public GlyphLayout
            ladiesText,
            infoText;

    public List<GlyphLayout> playerGlyphLayouts = new ArrayList<>();

    public PlayerState() {
        super();

        setBackgroundColor(new Color(0.2f, 0.1f, 0.3f, 1));

        titleFont = FontUtil.brittany(36);
        titleFont.setColor(new Color(1, 0.9f, 0.95f, 1));
        infoFont = FontUtil.brittany(20);
        infoFont.setColor(new Color(1, 0.9f, 0.95f, 0.9f));
        ladiesFont = FontUtil.brittany(28);
        ladiesFont.setColor(Color.WHITE.cpy());

        ladiesText = new GlyphLayout(titleFont, "LADIES");
        infoText = new GlyphLayout(infoFont, "Press 'a' to join, 'start' to fight");
    }

    public boolean readyForPlayer() { return players.size() >= 2; }
    @Override public boolean shouldBeReused() { return false; }

    @Override
    public void onTransitionInStart(boolean firstTransition, Void aVoid) {
        super.onTransitionInStart(firstTransition, aVoid);

        for (Controller controller: Controllers.getControllers()) {
            Pair<? extends Controller, PlayerControllerListener> pair = new Pair<>(controller, new PlayerControllerListener());
            controllerListenerPairs.add(pair);
            pair.first.addListener(pair.second);
        }
    }

    @Override
    public void onTransitionOutFinish() {
        super.onTransitionOutFinish();

        for (Pair<? extends Controller, ? extends ControllerListener> pair: controllerListenerPairs) {
            pair.first.removeListener(pair.second);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        int halfWidth = Gdx.graphics.getWidth() / 2;

        // Ladies text
        float ladiesFontBottom = Gdx.graphics.getHeight() - PADDING;
        titleFont.draw(batch, ladiesText, halfWidth - ladiesText.width / 2, ladiesFontBottom);
        ladiesFontBottom -= PADDING + ladiesText.height;

        // Info text
        float infoBottom = PADDING + infoText.height;
        infoFont.draw(batch, infoText, PADDING, infoBottom);
        float infoTop = infoBottom + PADDING;

        // Ladies cells
        float cellTop = ladiesFontBottom;
        if (players.size() > 0) {
            float heightPerCell = (ladiesFontBottom - infoTop) / players.size();
            float ladyCellImgWidth = Math.max(Gdx.graphics.getWidth() * 0.2f, MIN_LADY_CELL_IMG_WIDTH);
            for (int i = 0, n = players.size(); i < n; i++) {
                Player player = players.get(i);

                // Cell background
                float avg = (player.damsel.color.r + player.damsel.color.g + player.damsel.color.b) / 3f;
                batch.setColor(
                        avg * 0.1f + player.damsel.color.r * 0.1f,
                        avg * 0.1f + player.damsel.color.g * 0.1f,
                        avg * 0.1f + player.damsel.color.b * 0.1f,
                        1
                );
                batch.draw(Textures.WHITE_PIXEL, 0, cellTop - heightPerCell, Gdx.graphics.getWidth(), heightPerCell);
                batch.setColor(0xFFFFFFFF);

                // Lady


                // Text
                GlyphLayout glyphLayout = playerGlyphLayouts.get(i);
                float textTop = cellTop - heightPerCell / 2 + glyphLayout.height / 2;
                ladiesFont.draw(batch, glyphLayout, ladyCellImgWidth + PADDING, textTop);

                cellTop -= heightPerCell;
            }
        }
    }
    @Override
    public void update(float delta) {
        for (Pair<? extends Controller, PlayerControllerListener> pair : controllerListenerPairs) {
            if (pair.second.requestingPlayer) {
                pair.second.requestingPlayer = false;
                if (pair.second.player == null) {
                    players.add(pair.second.player = new Player(pair.first));
                    playerGlyphLayouts.add(
                            players.indexOf(pair.second.player),
                            new GlyphLayout(
                                    ladiesFont,
                                    pair.second.player.damsel.name
                            )
                    );
                } else {
                    int index = players.indexOf(pair.second.player);
                    players.remove(index);
                    playerGlyphLayouts.remove(index);
                    pair.first.removeListener(pair.second.player);
                    pair.second.player = null;
                }
            }
        }
    }

    public class PlayerControllerListener extends ControllerAdapter {
        public Player player;
        public boolean dead = false, requestingPlayer = false;
        @Override
        public boolean buttonDown(Controller controller, int buttonIndex) {
            if (dead || requestingPlayer) return false;
            switch (XboxMappings.Button.getButton(buttonIndex)) {
                case START:
                    // Require at least two player before being available
                    if (!readyForPlayer()) return false;
                    dead = true;
                    switchToState(GameState.class, Color.WHITE, TRANSITION_MEDIUM, players);
                    break;
                case A:
                    requestingPlayer = true;
                    break;
            }
            return false;
        }
    }
}
