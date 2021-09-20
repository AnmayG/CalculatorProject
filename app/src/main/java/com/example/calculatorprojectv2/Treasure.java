package com.example.calculatorprojectv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Treasure extends AppCompatActivity {

    private Button freezePower, doublePower, clickPower, exit;
    private TextView pointsLbl;
    private int points;
    private int freezeCost = 50;
    private int doubleCost = 100;
    private int clickCost = 20;
    private int freeze_quant = 0;
    private int double_quant = 0;
    private int click_quant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);

        String pts = getIntent().getStringExtra("Points");
        points = Integer.parseInt(pts);

        freezePower = findViewById(R.id.freezeTimer);
        doublePower = findViewById(R.id.doublePoints);
        clickPower = findViewById(R.id.addClicks);
        exit = findViewById(R.id.exitBtn);
        pointsLbl = findViewById(R.id.pointCount);

        try{
            freeze_quant = Integer.parseInt(getIntent().getStringExtra("NumFreeze"));
            click_quant = Integer.parseInt(getIntent().getStringExtra("NumClick"));
            double_quant = Integer.parseInt(getIntent().getStringExtra("NumDouble"));
            System.out.println(freeze_quant + ", " + click_quant + ", " + double_quant);
        }catch(Exception e){
            System.out.println("hadn't been powerups bought");
        }

        clickBtns();
        updatePointsLabel();
    }

    private void updatePointsLabel() {
        String displayText = "Points: " + points;
        pointsLbl.setText(displayText);
    }

    private void clickBtns() {
        freezePower.setOnClickListener(View -> {
            if(points >= freezeCost){
                points -= freezeCost;
                freeze_quant++;
                updatePointsLabel();
            }
        });

        doublePower.setOnClickListener(View -> {
            if(points >= doubleCost){
                points -= doubleCost;
                double_quant++;
                updatePointsLabel();
            }
        });

        clickPower.setOnClickListener(View ->{
            if(points >= clickCost){
                points -= clickCost;
                click_quant++;
                updatePointsLabel();
            }
        });

        exit.setOnClickListener(View -> {
            Intent intent;
            if(getIntent().getStringExtra("PrevScreen").equals("EndScreen")){
                intent = new Intent(this, EndScreen.class);
                intent.putExtra("PointsAdded", getIntent().getStringExtra("PointsAdded"));
            }
            else{
                intent = new Intent(this, GameTypeActivity.class);
            }
            intent.putExtra("Points", points +"");
            intent.putExtra("NumFreeze", freeze_quant+"");
            intent.putExtra("NumDouble", double_quant+"");
            intent.putExtra("NumClick", click_quant+"");
            startActivity(intent);
        });
    }
}