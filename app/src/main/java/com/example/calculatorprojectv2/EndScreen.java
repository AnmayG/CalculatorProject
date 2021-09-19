package com.example.calculatorprojectv2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EndScreen extends AppCompatActivity {

    private TextView currentScoreTxt, highScoreTxt;
    private Button replayGame, doubleScorePowerupQuestion;
    private int numDoublePowerup = 0;
    private boolean doublePointsEnabled = false;
    private PowerUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        doubleScorePowerupQuestion = findViewById(R.id.double_points_powerup2);

        viewModel = new ViewModelProvider(this).get(PowerUpViewModel.class);
        numDoublePowerup = viewModel.getNumDouble();
        if(numDoublePowerup > 0) {
            doubleScorePowerupQuestion.setVisibility(View.VISIBLE);
        } else {
            doubleScorePowerupQuestion.setVisibility(View.GONE);
        }

        currentScoreTxt = findViewById(R.id.gameScoreLabel);
        highScoreTxt = findViewById(R.id.highScoreLabel);

        String displayTxt = "Game Score: "  + viewModel.getPointsAdded();
        currentScoreTxt.setText(displayTxt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                String highScore = "High Score: " + value;
                highScoreTxt.setText(highScore);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        doubleScorePowerupQuestion.setOnClickListener(view ->{
            if(numDoublePowerup > 0){
                numDoublePowerup-= 1;
                System.out.println("numdoublePowerupNow");
                doublePointsEnabled = true;
                doubleScorePowerupQuestion.setVisibility(View.GONE);
            }
        });

        replayGame = findViewById(R.id.singlePlayerBtn);
        replayGame.setOnClickListener(view ->{
            viewModel.setPrevScreen("EndScreen");
            viewModel.setIsDoublePointsEnabled(doublePointsEnabled);

            Intent intent = new Intent(this, LevelOneActivity.class);
            startActivity(intent);
        });
    }
}