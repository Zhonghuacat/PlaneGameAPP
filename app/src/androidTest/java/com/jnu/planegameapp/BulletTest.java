package com.jnu.planegameapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Bullet_big;
import com.jnu.planegameapp.gamedata.Bullet_enemy;
import com.jnu.planegameapp.gamedata.Bullet_small;
import com.jnu.planegameapp.gamedata.Enemy;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BulletTest {

    @Test
    public void move(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Bullet> bullets = new ArrayList<>();
        //前两个是我方子弹，从下往上运动40，后面的是敌方子弹，从上往下运动8
        Bullet bullet_small = new Bullet_small(context,0,0);
        Bullet bullet_big = new Bullet_big(context,0,0);
        Bullet bullet_enemy = new Bullet_enemy(context,0,0);
        bullets.add(bullet_small);
        bullets.add(bullet_big);
        bullets.add(bullet_enemy);

        for (Bullet bullet : bullets) {
            bullet.move();
        }
        assertEquals(-40f,bullet_small.getY(),1e-1);
        assertEquals(-40f,bullet_big.getY(),1e-1);
        assertEquals(8f,bullet_enemy.getY(),1e-1);
    }

}
