package com.example.calculatorprojectv2;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button mainButton, nextButton, skipButton;
    TextView instructionsLabel, highScore;
   // ImageView treasureChest;
    private int stage = 0;
    public int points = 100;

    private PowerUpViewModel viewModel;

    //TO FIX: points always reset to 100 when new main activity started

    private final String[] stageLabels = {
            "You're bored in class one day, so you take our your calculator",
            "But you notice something's a little off about it...",
            "YOUR CALCULATOR IS BROKEN!",
            "But since you are the civilized person that you are",
            "Instead of panicking, you decide to take this opportunity to cure your boredom!",
            "Your goal is to get to the target button while staying under the button click limit.",
            "Press the Play Button to start!"
    };

    // Bluetooth is implemented by following
    // https://developer.android.com/guide/topics/connectivity/bluetooth
    // So there's a lot of stuff that I'm missing out on as I'm just copy pasting code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = findViewById(R.id.mainButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        instructionsLabel = findViewById(R.id.instructionLabelOne);
        highScore = findViewById(R.id.highScore);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                String highScoreTxt = "High Score: " + value;
                highScore.setText(highScoreTxt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        mainButton.setOnClickListener(view -> openSelectGameType());

        nextButton.setOnClickListener(view -> {
            instructionsLabel.setText(stageLabels[stage]);
            if (stage == stageLabels.length - 1) {
                nextButton.setVisibility(View.GONE);
                mainButton.setVisibility(View.VISIBLE);
                skipButton.setVisibility(View.GONE);
            }
            stage++;
        });

        skipButton.setOnClickListener(view -> {
            instructionsLabel.setText(stageLabels[stageLabels.length - 1]);
            nextButton.setVisibility(View.GONE);
            mainButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.GONE);

        });

        viewModel = new ViewModelProvider(this).get(PowerUpViewModel.class);
    }


    public void openSelectGameType(){
        viewModel.setPoints(points);
        Intent intent = new Intent(this, GameTypeActivity.class);
        startActivity(intent);
    }

    //FIX CODE TO READ DATA TMRW!!! sds

//    private void handleLoadPrevScores(ActionEvent event) {
//        try{
//            FileReader reader = new FileReader("src/main/res/user_scores.txt");
//            Scanner in = new Scanner(reader);
//            while(in.hasNextLine())
//            {
//                String temp = in.nextLine();
//            }
//        } catch (FileNotFoundException ex) {
//            System.out.println("SOMETHING HAS GONE HORRIBLY WRONG WE'RE ALL GONNA DIE!");
//        }
//    }
//
//    private void handleSaveNewScore(ActionEvent event){
//        String outFile = "src/main/res/user_scores.txt";
//        try {
//            PrintWriter out = new PrintWriter(outFile);
//            out.close();
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Something went wrong!");
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
