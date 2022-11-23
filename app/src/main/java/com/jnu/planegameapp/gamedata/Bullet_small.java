package com.jnu.planegameapp.gamedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.jnu.planegameapp.R;

public class Bullet_small implements Bullet{

    private Bitmap bitmap;
    private float x,y;
    private int ATK=1;
    private boolean Hit=false;
    private float direction= (float) (Math.PI/2);

    Paint paint=new Paint();
    public Bullet_small(Context context, float x, float y) {
        this.bitmap = getBitmap(context);
        initPosition(x,y);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.setScale(0.08f,0.16f);
        // 得到新的圖片
        Bitmap new_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return new_bitmap;
    }

    private void initPosition(float x,float y) {
        this.x=x;
        this.y=y;
    }

    //自动移动
    public void move(){
        y-=40;
    }

    public void draw(@NonNull Canvas canvas){
        //居中
        canvas.drawBitmap(bitmap,x-bitmap.getWidth()/2,y-bitmap.getHeight()/2,paint);
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public int getATK() {
        return ATK;
    }

    public boolean getHit(){
        return Hit;
    }

    public void setHit(boolean b){
        this.Hit=b;
    }

    @Override
    public void setDirection(float direction) {
        this.direction = direction;
    }
}
