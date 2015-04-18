package com.dagothig.knightfight.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dagothig.knightfight.res.ImageSheet;
import com.dagothig.knightfight.res.Textures;

/**
 * Created by dagothig on 11/14/14.
 */
public abstract class SquareMenuButton extends MenuButton {
    public static final int
            STATE_NORMAL_FRAME_Y = 0,
            STATE_HOVERED_FRAME_Y = 1,
            STATE_DOWN_FRAME_Y = 2,
            STATE_INACTIVE_FRAME_Y = 3;

    protected int posX, posY, width, height;
    protected ImageSheet imageSheet;
    public Color tint;

    public SquareMenuButton(ImageSheet.Definition sheetDefinition, int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.imageSheet = Textures.get(sheetDefinition);
    }
    public SquareMenuButton(ImageSheet.Definition sheetDefinition) {
        this(sheetDefinition, 0, 0, sheetDefinition.frameWidth, sheetDefinition.frameHeight);
    }

    public int getTop() {
        return posY + height;
    }
    public int getBottom() {
        return posY;
    }
    public int getHeight() {
        return height;
    }

    public void setPosWithLeftX(int x) {
        posX = x;
    }
    public void setPosWithCenterX(int x) {
        posX = x - width / 2;
    }
    public void setPosWithRightX(int x) {
        posX = x - width;
    }

    public int getLeft() {
        return posX;
    }
    public int getRight() {
        return posX + width;
    }
    public int getWidth() {
        return width;
    }

    public void setPosWithTopY(int y) {
        posY = y;
    }
    public void setPosWithCenterY(int y) {
        posY = y - height / 2;
    }
    public void setPosWithBottomY(int y) {
        posY = y - height;
    }

    @Override
    public boolean isMouseWithin(int x, int y) {
        return x >= posX
            && x <= posX + width
            && y >= posY
            && y <= posY + height;
    }

    @Override
    public void render(SpriteBatch batch) {
        int imageY;
        switch (getState()) {
            case INACTIVE:
                imageY = STATE_INACTIVE_FRAME_Y;
                break;
            case HOVERED:
                imageY = STATE_HOVERED_FRAME_Y;
                break;
            case DOWN:
                imageY = STATE_DOWN_FRAME_Y;
                break;
            default:
                imageY = STATE_NORMAL_FRAME_Y;
                break;
        }

        imageSheet.render(
                batch,
                posX - imageSheet.getFrameWidth() / 2 + width / 2,
                posY - imageSheet.getFrameHeight() / 2 + height / 2,
                0, imageY,
                1, false,
                tint != null ? tint : Color.WHITE
        );
    }
}
