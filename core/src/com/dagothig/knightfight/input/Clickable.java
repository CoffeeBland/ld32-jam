package com.dagothig.knightfight.input;

/**
 * Created by dagothig on 11/2/14.
 */
public interface Clickable {
    public float getClickPriority();

    public boolean onMouseDown(int button, int x, int y);
    public boolean onMouseUp(int button, int x, int y);
    public boolean onMouseMove(int x, int y);
    public void update(float delta);
}
