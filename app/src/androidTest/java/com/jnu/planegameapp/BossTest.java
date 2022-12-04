package com.jnu.planegameapp;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Plane_boss;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BossTest {

    @Test
    public void move() {
    }

    @Test
    public void createBulletLeft() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        //限制的有时间、子弹数、攻击数 攻击数为0时会重置state和攻击数
        //符合的等价类1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类2 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类3 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //时间上不符合的等价类2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,8,bullets);
        assertEquals(0,bullets.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,-12,bullets);
        assertEquals(1,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(0,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(0,bullets.size());

    }

    @Test
    public void createBulletLeftState() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);

        //测试重置的功能 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletLeft(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletLeft(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

    }

    @Test
    public void createBulletRight() {
    }

    @Test
    public void createBulletMid() {
    }

    @Test
    public void createBullet() {
    }

    @Test
    public void createEnemies() {
    }

    @Test
    public void createMissile() {
    }




}
