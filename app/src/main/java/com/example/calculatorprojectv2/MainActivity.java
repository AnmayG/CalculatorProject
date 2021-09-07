package com.example.calculatorprojectv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mainButton, nextButton;
    TextView instructionsLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Animation animationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//        Animation animationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        //^^Fade in and Fade out animation through XML files

        mainButton = (Button) findViewById(R.id.mainButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        instructionsLabel = (TextView) findViewById(R.id.instructionLabelOne);

        final int[] stage = {0};

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLevelOne();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    protected AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
    protected AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

    private void fadeAnimation(TextView input){
        input.startAnimation(fadeIn);
        input.startAnimation(fadeOut);
        fadeIn.setDuration(1000);
        fadeOut.setDuration(1000);
        fadeIn.setFillAfter(true);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(4200 + fadeIn.getStartOffset());
    }
    //^^ Fade animation (play around with later)

    public void openLevelOne(){
        Intent intent = new Intent(this, LevelOneActivity.class);
        startActivity(intent);
    }

}
