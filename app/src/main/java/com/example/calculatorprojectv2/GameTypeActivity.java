package com.example.calculatorprojectv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class GameTypeActivity extends AppCompatActivity {

    private Button singlePlayer, collaborative, competitive, doublePowerup;
    private ImageView treasureChest;
    private int freezeCount = 0;
    private int clickCount = 0;
    private int doubleCount = 0;
    private Toast bluetoothNeeded;
    private int points;
    private boolean doublePointsEnabled = false;
    private final CharSequence btNeededText = "Please connect second device to play!";
    private PowerUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        viewModel = new ViewModelProvider(this).get(PowerUpViewModel.class);
        points = viewModel.getPoints();
        freezeCount = viewModel.getNumFreeze();
        clickCount = viewModel.getNumClick();
        doubleCount = viewModel.getNumClick();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        bluetoothNeeded = Toast.makeText(context, btNeededText, duration);

        singlePlayer = findViewById(R.id.singlePlayerBtn);
        collaborative = findViewById(R.id.collaborativeBtn);
        competitive = findViewById(R.id.competitiveBtn);
        doublePowerup = findViewById(R.id.double_points_powerup);

        if(doubleCount > 0){
            doublePowerup.setVisibility(View.VISIBLE);
        }

        singlePlayer.setOnClickListener(view -> openLevelOne());

        //change once bluetooth code implemented
        collaborative.setOnClickListener(view -> openNetworkActivity());

        competitive.setOnClickListener(view -> bluetoothNeeded.show());

        treasureChest = findViewById(R.id.treasure_image);
        treasureChest.setOnClickListener(view ->{
            System.out.println("clicked treasure");

            viewModel.setPrevScreen("GameType");

            Intent intent = new Intent(this, Treasure.class);
            startActivity(intent);
        });

        doublePowerup.setOnClickListener(view ->{
            doubleCount -= 1;
            doublePointsEnabled = true;
            doublePowerup.setVisibility(View.INVISIBLE);
        });
    }

    public void openLevelOne(){
        viewModel.setPoints(points);
        viewModel.setNumFreeze(freezeCount);
        viewModel.setNumDouble(doubleCount);
        viewModel.setNumClick(clickCount);
        viewModel.setIsDoublePointsEnabled(doublePointsEnabled);
        viewModel.setPrevScreen("GameType");

        Intent intent = new Intent(this, LevelOneActivity.class);
        startActivity(intent);
    }

    public void openNetworkActivity(){
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }
}