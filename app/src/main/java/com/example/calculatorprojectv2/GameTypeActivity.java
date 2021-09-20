package com.example.calculatorprojectv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameTypeActivity extends AppCompatActivity {

    private Button singlePlayer, collaborative, doublePowerup;
    private ImageView treasureChest;
    private int freezeCount = 0;
    private int clickCount = 0;
    private int doubleCount = 0;
    private int points;
    private boolean doublePointsEnabled = false;
    private final CharSequence btNeededText = "Please connect second device to play!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        String pts = getIntent().getStringExtra("Points");
        points = Integer.parseInt(pts);

        try{
            freezeCount = Integer.parseInt(getIntent().getStringExtra("NumFreeze"));
            clickCount = Integer.parseInt(getIntent().getStringExtra("NumClick"));
            doubleCount = Integer.parseInt(getIntent().getStringExtra("NumDouble"));
            doublePointsEnabled = Boolean.parseBoolean(getIntent().getStringExtra("isDoublePointsEnabled"));
            System.out.println(freezeCount + ", " + clickCount + ", " + doubleCount + ", " + doublePointsEnabled);
        }catch(Exception e){
            System.out.println("hadn't been powerups bought");
        }

        singlePlayer = findViewById(R.id.singlePlayerBtn);
        collaborative = findViewById(R.id.collaborativeBtn);
        doublePowerup = findViewById(R.id.double_points_powerup);

        if(doubleCount > 0){
            doublePowerup.setVisibility(View.VISIBLE);
        }

        singlePlayer.setOnClickListener(view -> openLevelOne());

        //change once bluetooth code implemented
        collaborative.setOnClickListener(view -> openNetworkActivity());

        treasureChest = findViewById(R.id.treasure_image);
        treasureChest.setOnClickListener(view ->{
            System.out.println("clicked treasure");

            Intent intent = new Intent(this, Treasure.class);
            intent.putExtra("Points", points + "");
            intent.putExtra("NumFreeze", freezeCount+"");
            intent.putExtra("NumDouble", doubleCount+"");
            intent.putExtra("NumClick", clickCount+"");
            intent.putExtra("PrevScreen", "gameType");
            startActivity(intent);
        });

        doublePowerup.setOnClickListener(view ->{
            doubleCount -= 1;
            doublePointsEnabled = true;
            doublePowerup.setVisibility(View.INVISIBLE);
        });
    }

    public void openLevelOne(){
        Intent intent = new Intent(this, LevelOneActivity.class);
        intent.putExtra("Points", points +"");
        intent.putExtra("NumFreeze", freezeCount+"");
        intent.putExtra("NumDouble", doubleCount+"");
        intent.putExtra("NumClick", clickCount+"");
        intent.putExtra("isDoublePointsEnabled", doublePointsEnabled + "");
        System.out.println("doublePointsEnabled = " + doublePointsEnabled);
        intent.putExtra("isNetwork", false);
        startActivity(intent);
    }

    public void openNetworkActivity(){
        Intent intent = new Intent(this, NetworkActivity.class);
        intent.putExtra("Points", points +"");
        intent.putExtra("NumFreeze", freezeCount+"");
        intent.putExtra("NumDouble", doubleCount+"");
        intent.putExtra("NumClick", clickCount+"");
        intent.putExtra("isDoublePointsEnabled", doublePointsEnabled + "");
        System.out.println("doublePointsEnabled = " + doublePointsEnabled);
        intent.putExtra("isNetwork", false);
        startActivity(intent);
    }
}