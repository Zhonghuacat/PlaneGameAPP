package com.jnu.planegameapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import com.jnu.planegameapp.gamedata.Buff;
import com.jnu.planegameapp.gamedata.Buff_ATK;
import com.jnu.planegameapp.gamedata.Buff_HP;
import com.jnu.planegameapp.gamedata.Buff_Protect;
import com.jnu.planegameapp.gamedata.Buff_bullet;
import com.jnu.planegameapp.gamedata.Buff_double;
import com.jnu.planegameapp.gamedata.Buff_speed;
import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Plane_player;


public class BuffTest {

    @Test
    public void getBuff(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Buff> buffs = new ArrayList<>();
        Plane_player player = new Plane_player(context);

        buffs.add(new Buff_ATK(context));
        buffs.add(new Buff_bullet(context));
        buffs.add(new Buff_double(context));
        buffs.add(new Buff_speed(context));

        for (Buff buff : buffs) {
            buff.getBuff(player);
        }
        assertEquals(1.25f,player.ATKRate,1e-3);
        assertTrue(player.bigBullet);
        assertEquals(2,player.doubleBullet);
        assertEquals(1,player.speedUp);
    }

    @Test
    public void move(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<Buff> buffs = new ArrayList<>();
        Plane_player player = new Plane_player(context);
        //速度为3*3 protect为2*3
        buffs.add(new Buff_ATK(context));
        buffs.add(new Buff_bullet(context));
        buffs.add(new Buff_double(context));
        buffs.add(new Buff_speed(context));
        buffs.add(new Buff_HP(context));
        buffs.add(new Buff_Protect(context));

        for (Buff buff : buffs) {
            buff.move();
        }
        assertEquals(9f,buffs.get(0).getY(),1e-1);
        assertEquals(9f,buffs.get(1).getY(),1e-1);
        assertEquals(9f,buffs.get(2).getY(),1e-1);
        assertEquals(9f,buffs.get(3).getY(),1e-1);
        assertEquals(9f,buffs.get(4).getY(),1e-1);
        assertEquals(6f,buffs.get(5).getY(),1e-1);

    }


}
