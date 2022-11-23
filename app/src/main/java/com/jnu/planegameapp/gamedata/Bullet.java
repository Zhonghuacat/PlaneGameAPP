package com.jnu.planegameapp.gamedata;

import android.graphics.Canvas;

public interface Bullet {
    void move();
    void draw(Canvas canvas);
    boolean getHit();
    void setHit(boolean b);
    float getY();
    float getX();
    int getATK();
    void setDirection(float direction);
}
