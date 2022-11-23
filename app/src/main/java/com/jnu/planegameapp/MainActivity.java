package com.jnu.planegameapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int bestScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        Button buttonStart = findViewById(R.id.button_start);
        Button buttonQuit = findViewById(R.id.button_quit);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ActivityGame.class);
                intent.putExtra("bestScore",bestScore);
                game.launch(intent);
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public ActivityResultLauncher game = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 666){
                        bestScore = result.getData().getIntExtra("bestScore",0);
                        save();
                    }
                }
            });

    private void save(){
        ObjectOutputStream outputStream = null;
        try{
            FileOutputStream fout =openFileOutput("bestScore.txt", MODE_PRIVATE);
            outputStream = new ObjectOutputStream(fout);
            outputStream.writeObject(bestScore);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void loadData(){
        ObjectInputStream inputStream = null;
        try{
            FileInputStream fin =openFileInput("bestScore.txt");
            inputStream = new ObjectInputStream(fin);
            bestScore = (int)inputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(inputStream != null)
                    inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}