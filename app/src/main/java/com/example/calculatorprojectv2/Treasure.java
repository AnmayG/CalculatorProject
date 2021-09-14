package com.example.calculatorprojectv2;

import androidx.appcompat.app.AppCompatActivity;

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

        clickBtns();
        updatePointsLabel();
    }

    private void updatePointsLabel() {
        String displayText = "Points: " + points;
        pointsLbl.setText(displayText);
    }

    private void clickBtns() {
        freezePower.setOnClickListener(View -> {
            if(points > freezeCost){
                points -= freezeCost;
                updatePointsLabel();
            }
        });

        doublePower.setOnClickListener(View -> {
            if(points > doubleCost){
                points -= doubleCost;
                updatePointsLabel();
            }
        });

        clickPower.setOnClickListener(View ->{
            if(points > clickCost){
                points -= clickCost;
                updatePointsLabel();
            }
        });

        exit.setOnClickListener(View -> {

        });
    }
}