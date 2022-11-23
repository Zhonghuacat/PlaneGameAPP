package com.jnu.planegameapp.gamedata;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface Enemy {

    void move();
    void draw(Canvas canvas);
    void beHit(float ATK);
    float getX();
    float getY();
    float getHP();
    int getScore();
    Bitmap getBitmap();
    int getBulletNumber();
    void setBulletNumber(int number);
    float getBulletDirection();
    void setBulletDirection(float bulletDirection);

}
