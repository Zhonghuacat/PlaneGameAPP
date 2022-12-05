package com.jnu.planegameapp.gamedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.jnu.planegameapp.R;

public class Bullet_enemy implements Bullet{

    private Bitmap bitmap;
    private float x,y;
    private float direction= (float) (Math.PI/2);
    private int ATK=1;
    private boolean Hit=false;
    public float speed=8;
    public int state=0;
    private int time=0;

    Paint paint=new Paint();
    public Bullet_enemy(Context context, float x, float y) {
        this.bitmap = getBitmap(context);
        initPosition(x,y);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_enemy);
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
        if (state==0){
            this.x+=Math.cos(direction)*speed;
            this.y+=Math.sin(direction)*speed;
        }else if (state==1){
            speed=0.5f;
            this.x+=Math.cos(direction)*speed;
            this.y+=Math.sin(direction)*speed;
            time++;
            if (time>200) { state=2;}
        }else if (state==2){
            speed=40;
            this.x+=Math.cos(direction)*speed;
            this.y+=Math.sin(direction)*speed;
        }
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

    public void setDirection(float direction) {
        this.direction = direction;
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

    public float getDirection() {
        return direction;
    }

    public void setATK(int ATK) {
        this.ATK = ATK;
    }

    public boolean isHit() {
        return Hit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
