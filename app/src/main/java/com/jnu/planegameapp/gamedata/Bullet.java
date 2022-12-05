package com.jnu.planegameapp.gamedata;

import android.graphics.Canvas;

public interface Bullet {
    void move();
    void draw(Canvas canvas);
    boolean getHit();
    void setHit(boolean b);
    float getY();
    float getX();
    void setY(float y);
    void setX(float x);
    int getATK();
    void setATK(int ATK);
    float getDirection();
    void setDirection(float direction);
}
