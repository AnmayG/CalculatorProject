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
    private PowerUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);

        freezePower = findViewById(R.id.freezeTimer);
        doublePower = findViewById(R.id.doublePoints);
        clickPower = findViewById(R.id.addClicks);
        exit = findViewById(R.id.exitBtn);
        pointsLbl = findViewById(R.id.pointCount);

        viewModel = new ViewModelProvider(this).get(PowerUpViewModel.class);
        points = viewModel.getPoints();
        freeze_quant = viewModel.getNumFreeze();
        click_quant = viewModel.getNumClick();
        double_quant = viewModel.getNumDouble();
        System.out.println(freeze_quant + ", " + click_quant + ", " + double_quant);

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
            viewModel.setPoints(points);
            viewModel.setNumFreeze(freeze_quant);
            viewModel.setNumDouble(double_quant);
            viewModel.setNumClick(click_quant);
            viewModel.setPrevScreen("Treasure");
            Intent intent = new Intent(this, GameTypeActivity.class);
            startActivity(intent);
        });
    }
}