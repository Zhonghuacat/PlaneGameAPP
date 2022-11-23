package com.jnu.planegameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jnu.planegameapp.gamedata.GameView;

public class ActivityGame extends AppCompatActivity {

    public int resultCode = 666;
    int bestScore;
    GameView gameView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        intent = getIntent();
        bestScore=intent.getIntExtra("bestScore",0);

        gameView = findViewById(R.id.gameView);
        gameView.bestScore[0] = bestScore;//传入最大值
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            bestScore=Math.max(gameView.score,gameView.bestScore[0]);
            intent.putExtra("bestScore",bestScore);
            setResult(resultCode,intent);
            finish();
        }
        return true;
    }

}