package com.example.calculatorprojectv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button mainButton, nextButton;
    TextView instructionsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = (Button) findViewById(R.id.mainButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        instructionsLabel = (TextView) findViewById(R.id.instructionLabelOne);

        final int[] stage = {0};

        mainButton.setOnClickListener(view -> openLevelOne());

        nextButton.setOnClickListener(view -> {
            if (stage[0] == 0){
                instructionsLabel.setText("You're bored in class one day, so you take out your calculator");
            } else if (stage[0] == 1){
                instructionsLabel.setText("But you notice something's a little off about it...");
            } else if (stage[0] == 2){
                instructionsLabel.setText("YOUR CALCULATOR IS BROKEN!");
            } else if (stage[0] == 3){
                instructionsLabel.setText("But since you are the civilized person that you are");
            } else if (stage[0] == 4){
                instructionsLabel.setText("Instead of panicking, you decide to take this opportunity to cure your boredom!");
            } else if (stage[0] == 5){
                instructionsLabel.setText("Your goal, is to get to the goal number, while staying under the button click limit");
            } else if (stage[0] == 6){
                nextButton.setVisibility(View.GONE);
                instructionsLabel.setText("Press the Play Button to Start!");
                mainButton.setVisibility(View.VISIBLE);
            }

            stage[0]++;
        });
    }

    public void openLevelOne(){
        Intent intent = new Intent(this, LevelOneActivity.class);
        startActivity(intent);
    }

}
