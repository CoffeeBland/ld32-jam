package com.dagothig.knightfight.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dagothig.knightfight.game.world.World;
import com.dagothig.knightfight.res.Definitions;
import com.dagothig.knightfight.res.SheetAnimator;
import com.dagothig.knightfight.res.Textures;
import com.dagothig.knightfight.util.VectorPool;
import com.dagothig.knightfight.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Created by dagothig on 4/18/15.
 */
public class Knight extends Person {

    public float standingShift, fallingShift;
    public float standingHeight, fallingHeight;
    public Damsel damsel;

    public Knight() {
        super(32, 200,
                KnightName.getRandomName().name(), new Color(
                        (float)Math.random() * 0.5f + 0.5f,
                        (float)Math.random() * 0.5f + 0.5f,
                        (float)Math.random() * 0.5f + 0.5f,
                        1
        ));
        mainTexture = new SheetAnimator(Definitions.KNIGHT, 12, true,
                new Pair<>(0, 2),
                new Pair<>(2, 0)
        );
        shadow = Textures.get("knight_shadow.png");

        mainShiftX = mainTexture.getImageSheet().getFrameWidth() / 2;
        mainShiftY = standingShift = shadow.getHeight() / 2 - 14;
        fallingShift = standingShift + 52;
        standingHeight = height;
        fallingHeight = height * 0.25f;
    }

    @Override
    public void reactToCollision(Person person, Vector3 personVel) {
        if (person instanceof Damsel && person.pos.z < pos.z) {
            damsel = (Damsel)person;
        }
        velocity.z = 0;
    }

    @Override
    public void update(float delta, @NotNull World world) {
        if (mainTexture.getFrameX() == 0) {
            if (pos.z > MIN_DISTANCE) {
                mainTexture.playAnimation(1, 0, 2, 0);
                changeHeight(world, (standingHeight + fallingHeight) / 2, mainShiftY = (standingShift + fallingShift) / 2);
            } else {
                changeHeight(world, standingHeight, standingShift);
            }
        } else if (mainTexture.getFrameX() == 2) {
            if (pos.z <= MIN_DISTANCE) {
                mainTexture.playAnimation(1, 1, 0, 0);
                changeHeight(world, (standingHeight + fallingHeight) / 2, mainShiftY = (standingShift + fallingShift) / 2);
            } else {
                changeHeight(world, fallingHeight, fallingShift);
            }
        }

        if (damsel != null) {
            if (!isWithinDistance(damsel, velocity) || pos.z - (damsel.pos.z + damsel.height) < -MIN_DISTANCE) {
                damsel = null;
            } else if (pos.z + 8 - (damsel.pos.z + damsel.height) < MIN_DISTANCE) {
                velocity.add((damsel.pos.x - pos.x) * 0.25f, (damsel.pos.y - pos.y) * 0.25f, 0);
            }
        }

        super.update(delta, world);
    }
    public void changeHeight(World world, float newHeight, float newShift) {
        if (height == newHeight || mainShiftY == newShift) return;

        if (newHeight < height) {
            height = newHeight;
            mainShiftY = newShift;
        } else {
            Vector3 heightChange = VectorPool.V3().set(0, 0, newHeight - height);

            if (!isInCollision(world, heightChange)) {
                height = newHeight;
                mainShiftY = newShift;
            }

            VectorPool.claim(heightChange);
        }
    }

    @Override
    public void renderMain(SpriteBatch batch, Vector3 pos) {
        batch.setColor(color);
        super.renderMain(batch, pos);
        batch.setColor(Color.WHITE);
    }

    @Override
    public int getFrameX(float orientation) {
        return mainTexture.getFrameX();
    }
}
