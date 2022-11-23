package com.jnu.planegameapp.gamedata;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint paint = new Paint();
    //子线程标志位
    private boolean mIsDrawing;

    private Plane_player plane_player;
    private List<Enemy> enemies=new ArrayList<>();
    private List<Bullet> bullets=new ArrayList<>();
    private List<Buff> buffs=new ArrayList<>();

    private int mouse_x,mouse_y;
    int screenWidth;
    int screenHeight;

    boolean hasBOSS=false;
    boolean hasBUFF=false;
    int time=0;
    public int score=0;
    int HP=3;
    int protectTime=200;
    int gameLevel=1;
    public int[] bestScore = new int[5];

    public GameView(Context context){
        this(context,null);
    }

    public GameView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public GameView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        initView();
        plane_player = new Plane_player(this.getContext());
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        create_buff();
        initMainPlane();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mIsDrawing = true;
        //开启子线程
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        mIsDrawing = false;
    }

    //游戏逻辑主体
    @Override
    public void run() {
        int enemyNumber=3;
        while(mIsDrawing){
            move_mainPlane();
            move_enemyPlane(enemyNumber,time);
            move_bullet(time);
            move_buff();
            drawSomething();
            time++;
            enemyNumber= (int) (2+ Math.log1p(score+10));
            if (HP<=0) initGame();
            levelUP();
            if (hasBOSS){
                bossStateAction();
            }
        }
    }

    private void levelUP() {
        if (protectTime>0) protectTime--;
        if (score >=0 && score<1000 ) gameLevel = 1;
        if (score >=1000 && score < 2000) gameLevel = 2;
        if (score >= 2000 && score < 5000) gameLevel = 3;
        if (score >= 5000 && score < 10000) gameLevel = 4;
        if (score >= 10000) gameLevel = 3 + score/5000;
    }

    private void move_bullet(int time) {
        //我方发射子弹
        if (time%(12-plane_player.speedUp)==0) {
            float positionX = plane_player.getX()-(float)(plane_player.getBitmap().getWidth()/2);
            float next=(plane_player.getBitmap().getWidth())/(float)(plane_player.doubleBullet+1);
            if (plane_player.bigBullet){
                for (int i=0;i<plane_player.doubleBullet;i++){
                    positionX+=next;
                    bullets.add(new Bullet_big(this.getContext(),positionX, plane_player.getY()));
                }
            }else {
                for (int i=0;i<plane_player.doubleBullet;i++){
                    positionX+=next;
                    bullets.add(new Bullet_small(this.getContext(),positionX, plane_player.getY()));
                }
            }
        }
        //敌方发射子弹
        for (Enemy enemy : enemies){
            if (enemy.getClass()==Plane_enemy2.class){
                createEnemyBullets(Math.min(gameLevel,4),enemy,time);
            }
            if (enemy.getClass()==Plane_enemy3.class){
                createEnemyBullet(Math.min(gameLevel*2,8),enemy,time);
            }
        }
        //移动并判断是否命中
        for (Bullet bullet : bullets) {
            bullet.move();
            if (bullet.getClass()==Bullet_enemy.class){//敌方子弹
                hitMainPlane(bullet);
            }else{//我方子弹
                for (Enemy enemy : enemies) {
                    hit(bullet,enemy);
                }
            }
        }
        //判断移除
        for (int i=bullets.size()-1;i>=0;i--){
            if (bullets.get(i).getY()>screenHeight || bullets.get(i).getX()>screenWidth ||
                    bullets.get(i).getY()<0 || bullets.get(i).getX()<0 || bullets.get(i).getHit())
                bullets.remove(i);
        }
    }

    private void move_enemyPlane(int number,int time) {
        //添加飞机
        if (enemies.size()<number && Math.random() < 0.01*gameLevel && gameLevel%5!=0 ) {
            if (score > 3000 && Math.random() < 0.01*gameLevel){
                enemies.add(new Missile(this.getContext()));
            } else if (score > 1200 && Math.random() < 0.02*gameLevel){
                enemies.add(new Plane_enemy3(this.getContext()));
            }else if (score > 500 && Math.random() < 0.01*gameLevel){
                enemies.add(new Plane_paper(this.getContext()));
            } else if (score > 200 && Math.random() < 0.1*gameLevel){
                enemies.add(new Plane_enemy2(this.getContext()));
            } else {
                enemies.add(new Plane_enemy(this.getContext()));
            }
        }
        if (gameLevel%5==0){//BOSS战斗
            if (!hasBOSS) {
                Plane_boss planeBoss = new Plane_boss(this.getContext());
                planeBoss.setHP((float)(2000*gameLevel/5));
                enemies.add(planeBoss);
                hasBOSS=true;
            }
        }
        //移动
        for (Enemy enemy : enemies) {
            enemy.move();
            addEnemyBullet(enemy);
            //飞弹纠正轨道
            if (enemy.getClass()==Missile.class){
                Missile missile = (Missile) enemy;
                missile.setDirection(getDegrees(missile));
                hitMissile(missile);//碰撞检测
            }
        }
        //判断移除
        for (int i=enemies.size()-1;i>=0;i--){
            //入侵
            if (enemies.get(i).getY()>screenHeight-200
                    && enemies.get(i).getClass()!=Plane_boss.class
                    && enemies.get(i).getClass()!=Missile.class) {
                enemies.remove(i);
                HP--;
            }else
            //击毁飞机
            if (enemies.get(i).getHP()<=0){
                if (enemies.get(i).getClass()==Plane_boss.class){
                    hasBOSS=false;
                }
                create_buff(1+(float)(enemies.get(i).getScore()/200));
                score+=enemies.get(i).getScore();
                enemies.remove(i);
            }else
            //导弹左右边界移除
            if (enemies.get(i).getClass()==Missile.class &&
                    ((enemies.get(i).getX()<-200)
                            ||(enemies.get(i).getX()>screenWidth+200)
                            ||(enemies.get(i).getY()>screenHeight-200))){
                enemies.remove(i);
            }
        }
    }

    private void addEnemyBullet(Enemy enemy) {
        //敌方添加子弹
        if (enemy.getClass()==Plane_enemy2.class && Math.random()<0.005){
            enemy.setBulletNumber(gameLevel/2);
        }
        if (enemy.getClass()==Plane_enemy3.class && Math.random()<0.002){
            enemy.setBulletNumber(gameLevel);
        }
        if (enemy.getClass()==Plane_boss.class && Math.random()<0.005){
            enemy.setBulletNumber(5*gameLevel);
        }
    }

    private void move_buff(){
        //移动并判断是否命中
        for (Buff buff : buffs) {
            buff.move();
            hitBuff(buff);
        }
        //判断移除
        for (int i=buffs.size()-1;i>=0;i--){
            if (buffs.get(i).getY()>screenHeight-200) buffs.remove(i);
            else if(buffs.get(i).getHit()) buffs.remove(i);
        }
    }

    private void move_mainPlane() {
        int path=20,path_x,path_y;
        float x_move=mouse_x-plane_player.getX();
        float y_move=mouse_y-plane_player.getY();

        path_x = (int) Math.min(Math.abs(x_move),path);
        path_y = (int) Math.min(Math.abs(y_move),path);

        if (x_move>0){
            plane_player.setX(plane_player.getX()+path_x);
        }else if (x_move<0) plane_player.setX(plane_player.getX()-path_x);
        if (y_move>0){
            plane_player.setY(plane_player.getY()+path_y);
        }else if (y_move<0) plane_player.setY(plane_player.getY()-path_y);
    }

    //绘图逻辑
    private void drawSomething() {
        try {
            Plane_boss planeBoss = null;
            //获得canvas对象
            mCanvas = mSurfaceHolder.lockCanvas();
            //绘制背景
            mCanvas.drawColor(Color.GRAY);
            plane_player.draw(mCanvas);
            for (Enemy enemy : enemies) {
                enemy.draw(mCanvas);
                if (enemy.getClass()==Plane_boss.class){
                    planeBoss = (Plane_boss) enemy;
                }
            }
            for (Bullet bullet : bullets) {
                bullet.draw(mCanvas);
            }
            for (Buff buff : buffs){
                buff.draw(mCanvas);
            }
            if (hasBOSS){
                int HP = 400*gameLevel;
                mCanvas.drawText("BOSS  "+HP+"/"+planeBoss.getHP(),screenWidth/2+200,50,paint);
            }
            mCanvas.drawText("分数："+score,5,50,paint);
            mCanvas.drawText("生命："+HP,5,100,paint);
            mCanvas.drawText("攻击力："+plane_player.ATKRate,5,150,paint);
            mCanvas.drawText("射速："+((plane_player.speedUp+5)/(float)5),5,200,paint);
            if (protectTime>0) mCanvas.drawText("无敌..."+protectTime/50+"s",5,250,paint);
            mCanvas.drawText("最高分数："+bestScore[0],5,350,paint);
            if (mCanvas != null){
                //释放canvas对象并提交画布
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }catch (Exception e){
        }
    }

    private void bossStateAction() {
        for (int i=0;i<enemies.size();i++){
            if (enemies.get(i).getClass()==Plane_boss.class){//BOSS逻辑
                Plane_boss planeBoss = (Plane_boss)(enemies.get(i));
                hitBossPlane(planeBoss);
                if (planeBoss.state>=0){
                    if (planeBoss.state!=3&&planeBoss.state!=5&&planeBoss.state!=7) hasBUFF=false;//用于只添加一个BUFF
                    switch (planeBoss.state){
                        case 0:planeBoss.createBulletLeft(this.getContext(),time,bullets);break;
                        case 1:planeBoss.createBulletRight(this.getContext(),time,bullets);break;
                        case 2:planeBoss.createBulletMid(this.getContext(),time,bullets);break;
                        case 3:
                        case 5:
                        case 7:
                            if (!hasBUFF){
                                create_buff(2.5f);
                                hasBUFF=true;}
                            break;
                        case 4:planeBoss.createEnemies(this.getContext(),time,enemies);break;
                        case 6:planeBoss.createMissile(this.getContext(),time,enemies);break;
                        case 8:planeBoss.createBullet(this.getContext(),time,bullets,getDegrees(planeBoss));break;
                        default:break;
                    }
                }
            }
        }
    }

    private void hit(Bullet bullet, Enemy enemy){
        float distance_x = bullet.getX()-enemy.getX();
        float distance_y = bullet.getY()-enemy.getY();
        if(Math.abs(distance_x)<enemy.getBitmap().getWidth()/2)
            if (Math.abs(distance_y)<enemy.getBitmap().getHeight()/2){
                enemy.beHit(bullet.getATK()*plane_player.ATKRate/(1+(float)(gameLevel/5)));
                bullet.setHit(true);
            }
    }

    private void hitBuff(Buff buff){
        float distance_x = buff.getX()-plane_player.getX();
        float distance_y = buff.getY()-plane_player.getY();
        if(Math.abs(distance_x)<plane_player.getBitmap().getWidth()/2)
            if (Math.abs(distance_y)<plane_player.getBitmap().getHeight()/2){
                buff.getBuff(plane_player);
                if (buff.getClass()==Buff_HP.class){HP++;}
                if (buff.getClass()==Buff_Protect.class){protectTime=500;HP++;}
                buff.setHit(true);
            }
    }

    private void hitMainPlane(Bullet bullet){
        float distance_x = bullet.getX()-plane_player.getX();
        float distance_y = bullet.getY()-plane_player.getY();
        if(Math.abs(distance_x)<plane_player.getBitmap().getWidth()/2)
            if (Math.abs(distance_y)<plane_player.getBitmap().getHeight()/2){
                if (protectTime<=0) {HP--; protectTime=80;}
                else if (protectTime>0){
                    protectTime-=40;
                }
                bullet.setHit(true);
            }
    }

    private void hitBossPlane(Plane_boss planeBoss){
        float distance_x = planeBoss.getX()-plane_player.getX();
        float distance_y = planeBoss.getY()-plane_player.getY();
        if(Math.abs(distance_x)<planeBoss.getBitmap().getWidth()/2)
            if (Math.abs(distance_y)<planeBoss.getBitmap().getHeight()/2){
                if (protectTime<=0 && !planeBoss.hit) {
                    HP-=gameLevel; protectTime=200;
                    plane_player.setX(screenWidth/2.0f);
                    plane_player.setY(screenHeight);
                    planeBoss.hit=true;
                    initMainPlane();
                }else if (!planeBoss.hit && protectTime>0){
                    protectTime=0;
                    planeBoss.hit=true;
                }
            }
    }

    private void hitMissile(Missile missile){
        float distance_x = missile.getX()-plane_player.getX();
        float distance_y = missile.getY()-plane_player.getY();
        if(Math.abs(distance_x)<missile.getBitmap().getWidth()/2)
            if (Math.abs(distance_y)<missile.getBitmap().getHeight()/2){
                if (protectTime<=0 && !missile.hit) {
                    HP-=gameLevel/2; protectTime=200;
                    plane_player.setX(plane_player.getX()-distance_x*5);
                    plane_player.setY(plane_player.getY()-distance_y*5);
                    missile.hit=true;
                }else if (!missile.hit && protectTime>0){
                    protectTime=0;
                    missile.hit=true;
                }
            }
    }

    private int getDegrees(Missile missile){
        float distance_x = plane_player.getX()-missile.getX();
        float distance_y = plane_player.getY()-missile.getY();
        float now_tan=distance_y/distance_x;
        if (missile.getBulletDirection()>0&&missile.getBulletDirection()<Math.PI){
            float missile_tan= (float) Math.tan(missile.getBulletDirection());
            if (missile_tan>0&&now_tan>0){
                if (now_tan>missile_tan) return 1;
                else return -1;
            }
            if (missile_tan>0&&now_tan<0){
                return 1;
            }
            if (missile_tan<0&&now_tan>0){
                return -1;
            }
            if (missile_tan<0&&now_tan<0){
                if (now_tan>missile_tan) return 1;
                else return -1;
            }
        }
        return 1;
    }

    private int getDegrees(Plane_boss planeBoss){
        float distance_x = plane_player.getX()-planeBoss.getX();
        float distance_y = plane_player.getY()-planeBoss.getY();
        float now_tan=distance_y/distance_x;
        if (planeBoss.getBulletDirection()>0&&planeBoss.getBulletDirection()<Math.PI){
            float missile_tan= (float) Math.tan(planeBoss.getBulletDirection());
            if (missile_tan>0&&now_tan>0){
                if (now_tan>missile_tan) return 1;
                else return -1;
            }
            if (missile_tan>0&&now_tan<0){
                return 1;
            }
            if (missile_tan<0&&now_tan>0){
                return -1;
            }
            if (missile_tan<0&&now_tan<0){
                if (now_tan>missile_tan) return 1;
                else return -1;
            }
        }
        return 1;
    }

    private void create_buff(float rate){
        float get= (float) ((Math.random()*5)%5);
        switch ((int) get){
            case 0:if (Math.random()<0.1*rate) {
                buffs.add(new Buff_ATK(this.getContext()));
            }
                break;
            case 1:if(plane_player.doubleBullet<5){
                if (Math.random()<0.05*rate){
                    buffs.add(new Buff_double(this.getContext()));
                }
                break;
            }
            case 2:if(plane_player.speedUp<9) {
                if (Math.random()<0.05*rate){
                    buffs.add(new Buff_speed(this.getContext()));
                }
                break;
            }
            case 3:if(!plane_player.bigBullet) {
                if (Math.random()<0.01*rate){
                    buffs.add(new Buff_bullet(this.getContext()));
                }
                break;
            }
            case 4:if(Math.random()<0.1*rate) {
                if (Math.random()<0.2*rate) {buffs.add(new Buff_Protect(this.getContext()));}
                else {buffs.add(new Buff_HP(this.getContext()));}
            }
                break;
            default:
        }
    }

    private void create_buff(){
        float get= (float) ((Math.random()*4)%4);
        switch ((int) get){
            case 0: buffs.add(new Buff_ATK(this.getContext()));break;
            case 1: buffs.add(new Buff_double(this.getContext()));break;
            case 2: buffs.add(new Buff_speed(this.getContext()));break;
            case 3: buffs.add(new Buff_bullet(this.getContext()));break;
            default:break;
        }
    }

    private void createEnemyBullets(int number,Enemy enemy,int time){
        if (enemy.getBulletNumber()>0 && Math.random()<0.02 ){
            float turn = (float) Math.PI/32;
            float direction = (float) ((2*Math.random()+1)*Math.PI/4);
            if (plane_player.getX()>enemy.getX()) turn *= -1;
            for (int i=0;i<number && enemy.getBulletNumber()>0;i++){
                enemy.setBulletNumber(enemy.getBulletNumber()-1);
                Bullet bullet = new Bullet_enemy(this.getContext(),enemy.getX(),enemy.getY());
                bullet.setDirection(direction);
                direction+=turn;
                bullets.add(bullet);
            }
        }
    }

    private void createEnemyBullet(int number,Enemy enemy,int time) {
        if ( enemy.getBulletNumber()>0 && time %15==0){//有子弹 按一定间隔发射
            if (enemy.getBulletNumber()==number){//新发射子弹
                float direction = (float) ((2*Math.random()+1)*Math.PI/4);
                enemy.setBulletDirection(direction);
                enemy.setBulletNumber(enemy.getBulletNumber()-1);
                Bullet bullet = new Bullet_enemy(this.getContext(), enemy.getX(), enemy.getY());
                bullet.setDirection(direction);
                bullets.add(bullet);
            }else {
                float turn;
                if (Math.random()>0.5) turn = (float) Math.PI/32;
                else turn = -(float) Math.PI/32;
                if (enemy.getBulletDirection()>(Math.PI*3/4)) turn = -(float) Math.PI/32;
                if (enemy.getBulletDirection()<(Math.PI/4)) turn = (float) Math.PI/32;
                float direction = enemy.getBulletDirection()+turn;
                enemy.setBulletDirection(direction);
                enemy.setBulletNumber(enemy.getBulletNumber()-1);
                Bullet bullet = new Bullet_enemy(this.getContext(), enemy.getX(), enemy.getY());
                bullet.setDirection(direction);
                bullets.add(bullet);
            }
        }
    }

    private void initGame(){
        HP=3;
        bestScore[0] = Math.max(bestScore[0],score);
        score=0;
        time=0;
        gameLevel=1;
        protectTime=200;
        hasBOSS=false;
        plane_player.speedUp=0;
        plane_player.bigBullet=false;
        plane_player.doubleBullet=1;
        plane_player.ATKRate=1.0f;
        enemies.clear();
        bullets.clear();
        buffs.clear();
        plane_player=null;
        plane_player=new Plane_player(this.getContext());
        initMainPlane();
        create_buff();
    }

    private void initView(){
        mSurfaceHolder = getHolder();
        //注册回调方法
        mSurfaceHolder.addCallback(this);
        //设置一些参数方便后面绘图
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
    }

    private void initMainPlane() {
        mouse_x=screenWidth >> 1;
        mouse_y=screenHeight >> 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mouse_x = (int) event.getX();
        mouse_y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}
