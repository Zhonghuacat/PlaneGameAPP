package com.jnu.planegameapp;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Enemy;
import com.jnu.planegameapp.gamedata.Plane_boss;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BossTest {

    @Test
    public void move() {
        //利用因果图和判定表 排除不可能出现的测试用例
        //状态-1只出现在位置2，状态357上升阶段只出现在位置2，下落阶段出现在2-3
        /*  状态   -1       357     357
        *   方向   null     1       -1
        *   位置   2        2       2-3  位置2是 -(1/2)*Height  (1/6)*Height
        *   动作   下降     上升     下落  位置3是  (1/6)*Height  2*Height
        *   速度    4       8       30
        *  */
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        float Height = plane_boss.getBitmap().getHeight();

        plane_boss.state=-1;
        plane_boss.setY(-(1/2.f)*Height);
        plane_boss.move();
        assertEquals(-(1/2.f)*Height+4,plane_boss.getY(),1e-5);

        plane_boss.state=3;
        plane_boss.setDirection(1);
        plane_boss.setY((1/6.f)*Height);
        plane_boss.move();
        assertEquals((1/6.f)*Height-8,plane_boss.getY(),1e-5);

        plane_boss.state=3;
        plane_boss.setDirection(-1);
        plane_boss.setY(-(1/2.f)*Height);
        plane_boss.move();
        assertEquals(-(1/2.f)*Height+30,plane_boss.getY(),1e-5);

        plane_boss.state=5;
        plane_boss.setDirection(1);
        plane_boss.setY((1/6.f)*Height);
        plane_boss.move();
        assertEquals((1/6.f)*Height-8,plane_boss.getY(),1e-5);

        plane_boss.state=5;
        plane_boss.setDirection(-1);
        plane_boss.setY(-(1/2.f)*Height);
        plane_boss.move();
        assertEquals(-(1/2.f)*Height+30,plane_boss.getY(),1e-5);

        plane_boss.state=7;
        plane_boss.setDirection(1);
        plane_boss.setY((1/6.f)*Height);
        plane_boss.move();
        assertEquals((1/6.f)*Height-8,plane_boss.getY(),1e-5);

        plane_boss.state=7;
        plane_boss.setDirection(-1);
        plane_boss.setY(-(1/2.f)*Height);
        plane_boss.move();
        assertEquals(-(1/2.f)*Height+30,plane_boss.getY(),1e-5);

    }

    @Test
    public void createBulletLeft() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        //限制的有时间、子弹数、攻击数 攻击数为0时会重置state和攻击数
        //符合的等价类1
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

        //测试重置的功能 状态不改变 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(2);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(2);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletLeft(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变

    }

    @Test
    public void createBulletRight() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        //限制的有时间、子弹数、攻击数 攻击数为0时会重置state和攻击数
        //符合的等价类1
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类2 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类3 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //时间上不符合的等价类2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,8,bullets);
        assertEquals(0,bullets.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,-12,bullets);
        assertEquals(1,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(0,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(0,bullets.size());

        //测试状态变化----
        //测试重置的功能 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletRight(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletRight(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(2);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(2);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletRight(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变

    }

    @Test
    public void createBulletMid() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        //限制的有时间、子弹数、攻击数 攻击数为0时会重置state和攻击数
        //符合的等价类1
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类2 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类3 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(1,bullets.size());//--符合的等价类1

        //时间上不符合的等价类2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,8,bullets);
        assertEquals(0,bullets.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,-12,bullets);
        assertEquals(1,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(0,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(0,bullets.size());

        //测试状态变化----
        //测试重置的功能 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBulletMid(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBulletMid(context,12,bullets);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(2);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(2);
        plane_boss.setAttackNumber(25);
        plane_boss.createBulletMid(context,12,bullets);
        assertEquals(-1,plane_boss.state);//状态改变
    }

    @Test
    public void createBullet() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);
        //限制的有时间、子弹数、攻击数 攻击数为0时会重置state和攻击数
        //符合的等价类1
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类2 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(1,bullets.size());//--符合的等价类1

        //符合的等价类3 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(1,bullets.size());//--符合的等价类1

        //时间上不符合的等价类2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,6,bullets,1);
        assertEquals(0,bullets.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,-8,bullets,1);
        assertEquals(1,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(0,bullets.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(0,bullets.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(0,bullets.size());

        //测试状态变化----
        //测试重置的功能 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createBullet(context,8,bullets,1);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createBullet(context,8,bullets,1);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值1
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(2);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        bullets.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(2);
        plane_boss.setAttackNumber(25);
        plane_boss.createBullet(context,8,bullets,1);
        assertEquals(-1,plane_boss.state);//状态改变
    }

    @Test
    public void createEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);

        //符合的等价类1
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //符合的等价类2 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //符合的等价类3 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //时间上不符合的等价类2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,10,enemies);
        assertEquals(0,enemies.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,-20,enemies);
        assertEquals(1,enemies.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(0,enemies.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(0,enemies.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(0,enemies.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(0,enemies.size());

        //测试状态变化----
        //测试重置的功能 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createEnemies(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createEnemies(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(6);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(6);
        plane_boss.setAttackNumber(25);
        plane_boss.createEnemies(context,20,enemies);
        assertEquals(-1,plane_boss.state);//状态改变

    }

    @Test
    public void createMissile() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Plane_boss plane_boss = new Plane_boss(context);

        //符合的等价类1
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //符合的等价类2 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //符合的等价类3 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(1,enemies.size());//--符合的等价类1

        //时间上不符合的等价类2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,10,enemies);
        assertEquals(0,enemies.size());//--时间上不符合的等价类

        //时间为负的等价类3，危险
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,-20,enemies);
        assertEquals(1,enemies.size());

        //子弹数不符合的等价类4 边界值 子弹数0
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(0,enemies.size());

        //子弹数不符合的等价类4 边界值 子弹数-1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(-1);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(0,enemies.size());

        //攻击数不符合的等价类5 边界值 攻击数0
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(0,enemies.size());

        //攻击数不符合的等价类5 边界值 攻击数-1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(-1);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(0,enemies.size());

        //测试状态变化----
        //测试重置的功能 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(0);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(1);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值3
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(0);
        plane_boss.createMissile(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 边界值4
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(1);
        plane_boss.createMissile(context,20,enemies);
        assertNotEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值1
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(20);
        plane_boss.setAttackNumber(6);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(-1,plane_boss.state);//状态改变

        //测试重置的功能 状态不改变 边界值2
        enemies.clear();
        plane_boss.state=-1;
        plane_boss.setBulletNumber(6);
        plane_boss.setAttackNumber(25);
        plane_boss.createMissile(context,20,enemies);
        assertEquals(-1,plane_boss.state);//状态改变
    }

}
