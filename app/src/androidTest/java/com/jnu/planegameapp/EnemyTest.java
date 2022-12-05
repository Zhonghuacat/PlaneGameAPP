package com.jnu.planegameapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Enemy;
import com.jnu.planegameapp.gamedata.Missile;
import com.jnu.planegameapp.gamedata.Plane_enemy;
import com.jnu.planegameapp.gamedata.Plane_enemy2;
import com.jnu.planegameapp.gamedata.Plane_enemy3;
import com.jnu.planegameapp.gamedata.Plane_paper;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EnemyTest {

    @Test
    public void move(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Enemy> enemies = new ArrayList<>();
        //敌方普通飞机生成位置y=0，x为随机数
        Missile missile = new Missile(context);
        missile.setDirection(-1);
        enemies.add(missile);//负方向旋转角度 逆时针
        enemies.add(new Missile(context));//正方向旋转角度 顺时针 跟踪效果的实现
        enemies.add(new Plane_enemy(context));
        enemies.add(new Plane_enemy2(context));
        enemies.add(new Plane_enemy3(context));
        enemies.add(new Plane_paper(context));
        for (Enemy enemy : enemies) {
            enemy.move();
        }
        float direction1 = (float)((Math.PI/2.f)-(Math.PI/64.f));
        float direction2 = (float)((Math.PI/64.f)+(Math.PI/2.f));
        assertEquals(Math.sin(direction1)*12,enemies.get(0).getY(),1e-5);
        assertEquals(Math.sin(direction2)*12,enemies.get(1).getY(),1e-5);
        assertEquals(2f,enemies.get(2).getY(),1e-1);
        assertEquals(2f,enemies.get(3).getY(),1e-1);
        assertEquals(0.8f,enemies.get(4).getY(),1e-2);
        assertEquals(8f,enemies.get(5).getY(),1e-1);
    }

    @Test
    public void beHit(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Enemy> enemies = new ArrayList<>();
        //敌方普通飞机生成位置y=0，x为随机数
        enemies.add(new Missile(context));
        enemies.add(new Plane_enemy(context));
        enemies.add(new Plane_enemy2(context));
        enemies.add(new Plane_enemy3(context));
        enemies.add(new Plane_paper(context));

        for (Enemy enemy : enemies) {
            enemy.beHit(3f);//被攻击掉了3点血
        }
        assertEquals(17f,enemies.get(0).getHP(),1e-1);
        assertEquals(2f,enemies.get(1).getHP(),1e-1);
        assertEquals(17f,enemies.get(2).getHP(),1e-1);
        assertEquals(197f,enemies.get(3).getHP(),1e-1);
        assertEquals(2f,enemies.get(4).getHP(),1e-1);
    }

    @Test
    public void getScore(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Enemy> enemies = new ArrayList<>();
        //敌方普通飞机生成位置y=0，x为随机数
        enemies.add(new Missile(context));
        enemies.add(new Plane_enemy(context));
        enemies.add(new Plane_enemy2(context));
        enemies.add(new Plane_enemy3(context));
        enemies.add(new Plane_paper(context));

        assertEquals(0,enemies.get(0).getScore());
        assertEquals(10,enemies.get(1).getScore());
        assertEquals(50,enemies.get(2).getScore());
        assertEquals(200,enemies.get(3).getScore());
        assertEquals(100,enemies.get(4).getScore());
    }

}
