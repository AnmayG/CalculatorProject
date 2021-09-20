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
import android.widget.ImageView;
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
    private ImageView treasureChest;
    private int totalPoints = 0;
    private int numFreeze, numClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        doubleScorePowerupQuestion = findViewById(R.id.double_points_powerup2);
        doubleScorePowerupQuestion = findViewById(R.id.double_points_powerup2);
        numDoublePowerup = Integer.parseInt(getIntent().getStringExtra("NumDouble"));
        if(numDoublePowerup > 0){
            System.out.println("numdouble powerup: " + numDoublePowerup);
            doubleScorePowerupQuestion.setVisibility(View.VISIBLE);
        }

        currentScoreTxt = findViewById(R.id.gameScoreLabel);
        highScoreTxt = findViewById(R.id.highScoreLabel);

        String pts = getIntent().getStringExtra("PointsAdded");
        String displayTxt = "Game Score: "  + pts;
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
            Intent intent = new Intent(this, LevelOneActivity.class);
            try{
                numDoublePowerup = Integer.parseInt(getIntent().getStringExtra("NumDouble"));
                int numFreezePowerup = Integer.parseInt(getIntent().getStringExtra("NumFreeze"));
                int numClickPowerup = Integer.parseInt(getIntent().getStringExtra("NumClick"));
                intent.putExtra("NumFreeze", numFreezePowerup+"");
                intent.putExtra("NumDouble", numDoublePowerup+"");
                intent.putExtra("NumClick", numClickPowerup+"");
                intent.putExtra("isDoublePointsEnabled", doublePointsEnabled);
                System.out.println("Doubled points powerup: " + numDoublePowerup);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            String numTotalPts = getIntent().getStringExtra("Points");
            intent.putExtra("Points", numTotalPts);
            startActivity(intent);
        });

        treasureChest = findViewById(R.id.treasure_image);
        totalPoints = Integer.parseInt(getIntent().getStringExtra("Points"));
        numClick = Integer.parseInt(getIntent().getStringExtra("NumClick"));
        numFreeze = Integer.parseInt(getIntent().getStringExtra("NumFreeze"));

        treasureChest.setOnClickListener(view ->{
            System.out.println("clicked treasure");

            Intent intent = new Intent(this, Treasure.class);
            intent.putExtra("Points", totalPoints + "");
            intent.putExtra("PointsAdded", pts +"");
            intent.putExtra("NumFreeze", numFreeze+"");
            intent.putExtra("NumDouble", numDoublePowerup+"");
            intent.putExtra("NumClick", numClick+"");
            intent.putExtra("PrevScreen", "EndScreen");
            startActivity(intent);
        });
    }
}