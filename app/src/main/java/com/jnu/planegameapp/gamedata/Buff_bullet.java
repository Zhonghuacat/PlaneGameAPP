package com.jnu.planegameapp.gamedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.jnu.planegameapp.R;

public class Buff_bullet implements Buff{

    Paint paint=new Paint();
    int screenWidth;
    int screenHeight;
    private int direction=0;
    private float speed=3;
    private Bitmap bitmap;
    private float x,y;
    private boolean Hit=false;

    public Buff_bullet(Context context) {
        this.bitmap = getBitmap(context);
        initPosition(context);
    }

    @Override
    public void getBuff(Plane_player plane_player) {
        plane_player.bigBullet=true;
    }

    @Override
    public void move() {
        if(0==direction){
            x=(x+3*speed)%screenWidth;y=y+3*speed;
            direction=(int) (0.01+Math.random());
        }else if (1==direction){
            x=(x-3*speed)%screenWidth;y=y+3*speed;
            direction=(int) (0.99+Math.random());
        }
        if (x-(float)(bitmap.getWidth()/2)<0) direction=0;
        if (x+(float)(bitmap.getWidth()/2)>screenWidth) direction=1;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap,x-bitmap.getWidth()/2,y-bitmap.getHeight()/2,paint);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_big);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.setScale(0.15f,0.15f);
        matrix.postRotate(0f);
        // 得到新的圖片
        Bitmap new_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return new_bitmap;
    }

    private void initPosition(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        x= (float) (screenWidth*Math.random());
        y=0;
    }

    @Override
    public boolean getHit() {
        return Hit;
    }

    @Override
    public void setHit(boolean b) {
        this.Hit=b;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getX() {
        return x;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isHit() {
        return Hit;
    }
}
