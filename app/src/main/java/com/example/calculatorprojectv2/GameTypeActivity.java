package com.example.calculatorprojectv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class GameTypeActivity extends AppCompatActivity {

    private Button singlePlayer, collaborative, competitive;
    private Toast bluetoothNeeded;
    private final CharSequence btNeededText = "Please connect second device to play!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        bluetoothNeeded = Toast.makeText(context, btNeededText, duration);

        singlePlayer = findViewById(R.id.singlePlayerBtn);
        collaborative = findViewById(R.id.collaborativeBtn);
        competitive = findViewById(R.id.competitiveBtn);

        singlePlayer.setOnClickListener(view -> {
            openLevelOne();
        });

        //change once bluetooth code implemented
        collaborative.setOnClickListener(view -> bluetoothNeeded.show());

        competitive.setOnClickListener(view -> {
            openNetworkActivity();
        });
    }

    public void openLevelOne(){
        Intent intent = new Intent(this, LevelOneActivity.class);
        startActivity(intent);
    }

    public void openNetworkActivity(){
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }
}