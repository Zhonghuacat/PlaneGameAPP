package com.jnu.planegameapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.jnu.planegameapp.gamedata.Bullet;
import com.jnu.planegameapp.gamedata.Enemy;
import com.jnu.planegameapp.gamedata.GameView;
import com.jnu.planegameapp.gamedata.Missile;
import com.jnu.planegameapp.gamedata.Plane_boss;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void getDegrees(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GameView gameView = new GameView(context);

    }

}
