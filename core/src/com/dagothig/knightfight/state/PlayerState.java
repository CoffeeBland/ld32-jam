package com.dagothig.knightfight.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.Camera;
import com.dagothig.knightfight.game.Player;
import com.dagothig.knightfight.input.*;
import com.dagothig.knightfight.res.Textures;
import com.dagothig.knightfight.util.FontUtil;
import com.dagothig.knightfight.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.dagothig.knightfight.util.Dimensions.MIN_LADY_CELL_IMG_WIDTH;
import static com.dagothig.knightfight.util.Dimensions.PADDING;

public class PlayerState extends State<Void> {
    protected Camera camera = new Camera(); // just so dont create it in render 1000x times
    public List<Player> players = new ArrayList<>();
    public List<KnightFightKbdController> keyboardControllers = new ArrayList<>();
    public List<Pair<? extends KnightFightController, PlayerControllerListener>> controllerListenerPairs = new ArrayList<>();
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

        ladiesText = new GlyphLayout(titleFont, "Damsels");
        infoText = new GlyphLayout(infoFont, "Press 'a' to join, 'start' to fight ('space' and 'enter' on a keyboard)");
    }

    public boolean readyForPlayer() { return players.size() >= 1; }
    @Override public boolean shouldBeReused() { return false; }

    @Override
    public void onTransitionInStart(boolean firstTransition, Void aVoid) {
        super.onTransitionInStart(firstTransition, aVoid);

        // Add left keyboard controller
        KnightFightKbdController keyboardController = new KnightFightKbdController();
        Pair<? extends KnightFightController, PlayerControllerListener> kbdpair =
            new Pair<>(keyboardController, new PlayerControllerListener());
        kbdpair.first.addListener(kbdpair.second);
        controllerListenerPairs.add(kbdpair);
        keyboardControllers.add(keyboardController);

        // Add all plugged in xbox controller
        for (Controller controller: Controllers.getControllers()) {
            Pair<? extends KnightFightController, PlayerControllerListener> pair =
                new Pair<>(new KnightFightXboxController(controller), new PlayerControllerListener());
            controllerListenerPairs.add(pair);
            pair.first.addListener(pair.second);
        }
    }

    @Override
    public void onTransitionOutFinish() {
        super.onTransitionOutFinish();

        for (Pair<? extends KnightFightController, ? extends KnightFightControllerListener> pair: controllerListenerPairs) {
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
                batch.setColor(
                        player.damsel.color.r * 0.25f,
                        player.damsel.color.g * 0.25f,
                        player.damsel.color.b * 0.25f,
                        0.5f
                );
                batch.draw(Textures.WHITE_PIXEL, 0, cellTop - heightPerCell, Gdx.graphics.getWidth(), heightPerCell);
                batch.setColor(Color.WHITE);

                // Lady
                Vector3 pos = VectorPool.V3();
                pos.x = ladyCellImgWidth / 2;
                pos.y = cellTop - heightPerCell / 2 - player.damsel.getVisualHeight() / 2 + player.damsel.mainShiftY;
                player.damsel.render(batch, pos);
                VectorPool.claim(pos);

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
        for (Pair<? extends KnightFightController, PlayerControllerListener> pair : controllerListenerPairs) {
            if (pair.second.player != null && pair.second.player.damsel.mainTexture != null) {
                pair.second.player.damsel.mainTexture.update(delta);
                pair.second.player.damsel.orientation -= 0.0016f * delta;
            }

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

    public class PlayerControllerListener implements KnightFightControllerListener {
        public Player player;
        public boolean dead = false, requestingPlayer = false;
        @Override
        public boolean buttonDown(KnightFightController controller, KnightFightMappings.Button button) {
            if (dead || requestingPlayer) return false;

            switch (button) {
                case START:
                    // Require at least two player before being available
                    if (!readyForPlayer()) return false;
                    dead = true;
                    switchToState(GameState.class, Color.WHITE, TRANSITION_MEDIUM,
                        new Pair<>(players, keyboardControllers));
                    break;
                case JOIN:
                    requestingPlayer = true;
                    break;
            }
            return false;
        }

        @Override
        public boolean buttonUp(KnightFightController controller, KnightFightMappings.Button button) {
            return false;
        }

        @Override
        public boolean axisMoved(KnightFightController controller, KnightFightMappings.Axis axis, float value) {
            return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        for (KnightFightKbdController kfc : keyboardControllers) {
            kfc.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (KnightFightKbdController kfc : keyboardControllers) {
            kfc.keyDown(keycode);
        }
        return false;
    }
}
