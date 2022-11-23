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

public class Plane_paper implements Enemy{

    private Bitmap bitmap;
    private float x,y;
    private int direction=0;
    private float speed=8;
    private float HP=5;
    private int score=100;
    private int bulletNumber=0;
    private float bulletDirection = (float) (Math.PI/2);

    Paint paint=new Paint();
    int screenWidth;
    int screenHeight;

    public Plane_paper(Context context) {
        this.bitmap = getBitmap(context);
        initPosition(context);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane_speed);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.setScale(0.3f,0.3f);
        matrix.postRotate(90.0f);
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
        if(0==direction){
            x=(x+2*speed)%screenWidth;y=y+speed;
            direction=(int) (0.01+Math.random());
        }else if (1==direction){
            x=(x-2*speed)%screenWidth;y=y+speed;
            direction=(int) (0.99+Math.random());
        }
        if (x-(float)(bitmap.getWidth()/2)<0) direction=0;
        if (x+(float)(bitmap.getWidth()/2)>screenWidth) direction=1;
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
}
