package com.jnu.planegameapp.gamedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.jnu.planegameapp.R;

public class Missile implements Enemy{
    private Bitmap bitmap;
    private float x,y;
    private int direction=1;
    private float speed=12;
    private float HP=20;
    private int score=0;
    private int bulletNumber=0;
    private float bulletDirection = (float) (Math.PI/2);
    public boolean hit=false;

    Paint paint=new Paint();
    int screenWidth;
    int screenHeight;

    public Missile(Context context) {
        this.bitmap = getBitmap(context);
        initPosition(context);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.missile);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.setScale(0.3f,0.3f);
        matrix.postRotate(180.0f);
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

    //自动移动
    public void move(){
        y += Math.sin(bulletDirection)*speed;
        x += Math.cos(bulletDirection)*speed;

        float turn = (float) (Math.PI/64)*direction;
        bulletDirection += turn;
    }

    public void draw(@NonNull Canvas canvas){
        //居中
        canvas.drawBitmap(bitmap,x-bitmap.getWidth()/2,y-bitmap.getHeight()/2,paint);
    }

    public void beHit(float ATK){
        HP=HP-ATK;
    }

    public float getHP() {
        return HP;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getBulletNumber() {
        return bulletNumber;
    }

    public void setBulletNumber(int bulletNumber) {
        this.bulletNumber = bulletNumber;
    }

    public float getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(float bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
