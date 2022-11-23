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

import java.util.List;

public class Plane_boss implements Enemy{

    private Bitmap bitmap;
    private float x,y;
    private int direction=1;
    private float speed=1;
    private float HP=400;
    private int score=10000;
    private int bulletNumber=0;
    private int attackNumber=18;
    private float bulletDirection = (float) (Math.PI/2);
    public int state=-1;
    public boolean hit=false;
    public int gameLevel=5;
    int maxState=9;

    Paint paint=new Paint();
    int screenWidth;
    int screenHeight;

    public Plane_boss(Context context) {
        this.bitmap = getBitmap(context);
        initPosition(context);
    }

    private Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane_boss);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.setScale(1.5f,1.5f);
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
        x= (float) (screenWidth/2);
        y=(float)(-bitmap.getHeight()/2);
    }

    //自动移动
    public void move(){
        if (state==-1){
            if(y<(bitmap.getHeight()/6.0f)){//移动到静止位置
                y=y+4*speed;
            }else setState();//-1状态变为攻击状态
        }
        if (state==3){//阶段3
            if (direction==1){//上升阶段
                y=y-8*speed;
                if (y<(float)(-bitmap.getHeight()/2)){//改变阶段
                    speed *=30;
                    direction=-1;
                }
            } else if (direction==-1){//下落阶段
                y=y+2*speed;
                if (y>screenHeight*2){//恢复阶段
                    x= (float) (screenWidth/2);
                    y=(float)(-bitmap.getHeight()/2);
                    setState();
                    state=-1;
                }
            }
        }
        if (state==5){//阶段5
            if (direction==1){//上升阶段
                y=y-8*speed;
                if (y<(float)(-bitmap.getHeight()/2)){//改变阶段
                    speed*=30;
                    direction=-1;
                }
            } else if (direction==-1){//下落阶段
                y=y+2*speed;x=x+speed/3;
                if (y>screenHeight*1.5){//恢复阶段
                    x= (float) (screenWidth/2);
                    y=(float)(-bitmap.getHeight()/2);
                    setState();
                    state=-1;
                }
            }
        }
        if (state==7){//阶段5
            if (direction==1){//上升阶段
                y=y-8*speed;
                if (y<(float)(-bitmap.getHeight()/2)){//改变阶段
                    speed*=30;
                    direction=-1;
                }
            } else if (direction==-1){//下落阶段
                y=y+2*speed;x=x-speed/3;
                if (y>screenHeight*1.5){//恢复阶段
                    x= (float) (screenWidth/2);
                    y=(float)(-bitmap.getHeight()/2);
                    setState();
                    state=-1;
                }
            }
        }
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

    public void createBulletLeft(Context context,int time,List<Bullet> bullets){
        if (bulletNumber>0 && time%5==0 && attackNumber>0){
            float turn = -(float) Math.PI/32;
            if (bulletDirection<(Math.PI/4)) direction=-1;
            Bullet bullet = new Bullet_enemy(context, x-180, y+150);
            bullet.setDirection(bulletDirection);
            bullets.add(bullet);
            bulletDirection+=turn*direction;
            bulletNumber--;
            attackNumber--;
        }
        if (attackNumber<=0 || bulletNumber<=0) setState();
    }

    public void createBulletRight(Context context,int time,List<Bullet> bullets){
        if (bulletNumber>0 && time%5==0 && attackNumber>0){
            float turn = (float) Math.PI/32;
            if (bulletDirection>(Math.PI/4*3)) direction=-1;
            Bullet bullet = new Bullet_enemy(context, x+180, y+150);
            bullet.setDirection(bulletDirection);
            bullets.add(bullet);
            bulletDirection+=turn*direction;
            bulletNumber--;
            attackNumber--;
        }
        if (attackNumber<=0 || bulletNumber<=0) setState();
    }

    public void createBulletMid(Context context,int time,List<Bullet> bullets){
        if (bulletNumber>0 && time%5==0 && attackNumber>0){
            Bullet bullet = new Bullet_enemy(context, x, y+300);
            float RandomDirection = (float) ((2*Math.random()+1)*Math.PI/4.0f);
            bullet.setDirection(RandomDirection);
            bullets.add(bullet);
            bulletNumber--;
            attackNumber--;
        }
        if (attackNumber<=0 || bulletNumber<=0) setState();
    }

    public void createBullet(Context context,int time,List<Bullet> bullets,int direction){
        if (bulletNumber>0 && time%5==0 && attackNumber>0){
            Bullet_enemy bullet = new Bullet_enemy(context, x, y+300);
            float turn = (float)(Math.PI/64.0f);
            bulletDirection += turn*direction;
            bullet.setDirection(bulletDirection);
            bullet.state=1;
            bullets.add(bullet);
            bulletNumber--;
            attackNumber--;
        }
        if (attackNumber<=0 || bulletNumber<=0) setState();
    }

    public void createEnemies(Context context,int time,List<Enemy> enemies){
        if (bulletNumber>0 && time%8==0 && attackNumber>0){
            Enemy enemy = new Plane_paper(context);
            enemies.add(enemy);
            attackNumber-=5;
            bulletNumber-=5;
        }
        if (attackNumber<=0||bulletNumber<=0) setState();
    }

    public void createMissile(Context context,int time,List<Enemy> enemies){
        if (bulletNumber>0 && time%8==0 && attackNumber>0){
            Enemy enemy = new Missile(context);
            enemies.add(enemy);
            attackNumber-=5;
            bulletNumber-=5;
        }
        if (attackNumber<=0||bulletNumber<=0)
            setState();
    }

    private void setState() {
        hit=false;
        attackNumber=5*gameLevel;
        speed=1.0f;
        bulletDirection = (float) (Math.PI/2);
        direction = 1;
        state=(int) ((Math.random() * maxState) % maxState);
    }

    public void setHP(float HP) {
        this.HP = HP;
    }
}
