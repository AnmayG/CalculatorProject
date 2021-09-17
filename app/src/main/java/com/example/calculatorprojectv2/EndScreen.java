package com.example.calculatorprojectv2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EndScreen extends AppCompatActivity {

    private TextView currentScoreTxt, highScoreTxt;
    private Button replayGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        currentScoreTxt = findViewById(R.id.gameScoreLabel);
        highScoreTxt = findViewById(R.id.highScoreLabel);

        String pts = getIntent().getStringExtra("PointsAdded");
        String displayTxt = "Game Score: "  + pts;
        currentScoreTxt.setText(displayTxt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                String highScore = "High Score: " + value;
                highScoreTxt.setText(highScore);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        replayGame = findViewById(R.id.singlePlayerBtn);
        replayGame.setOnClickListener(view ->{
            Intent intent = new Intent(this, LevelOneActivity.class);

            try{
                int numDoublePowerup = Integer.parseInt(getIntent().getStringExtra("NumDouble"));
                int numFreezePowerup = Integer.parseInt(getIntent().getStringExtra("NumFreeze"));
                int numClickPowerup = Integer.parseInt(getIntent().getStringExtra("NumClick"));
                intent.putExtra("NumFreeze", numFreezePowerup+"");
                intent.putExtra("NumDouble", numDoublePowerup+"");
                intent.putExtra("NumClick", numClickPowerup+"");
            }
            catch(Exception e){

            }
            String numTotalPts = getIntent().getStringExtra("Points");
            intent.putExtra("Points", numTotalPts);
            startActivity(intent);
        });
    }
}