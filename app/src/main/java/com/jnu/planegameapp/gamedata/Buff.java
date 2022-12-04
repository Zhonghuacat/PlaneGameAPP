package com.jnu.planegameapp.gamedata;

import android.graphics.Canvas;

public interface Buff {
    void move();
    void draw(Canvas canvas);
    void getBuff(Plane_player plane_player);
    boolean getHit();
    void setHit(boolean b);
    float getY();
    float getX();
    void setY(float y);
    void setX(float x);
}
