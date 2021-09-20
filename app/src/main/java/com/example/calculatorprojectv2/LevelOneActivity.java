package com.example.calculatorprojectv2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mariuszgromada.math.mxparser.Expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LevelOneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        String points = getIntent().getStringExtra("Points");
        String doubleEnabled = getIntent().getStringExtra("isDoublePointsEnabled");
        String numDouble = getIntent().getStringExtra("NumDouble");
        String numFreeze = getIntent().getStringExtra("NumFreeze");
        String numClick = getIntent().getStringExtra("NumClick");

        Bundle args = new Bundle();
        args.putString("Points",points);
        args.putString("isDoublePointsEnabled", doubleEnabled);
        args.putString("NumDouble", numDouble);
        args.putString("NumFreeze", numFreeze);
        args.putString("NumClick", numClick);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.calculator_fragment, CalculatorFragment.class, args)
                .commit();

//        CalculatorFragment.newInstance(points, doubleEnabled, numDouble, numFreeze, numClick)
//                .setArguments(points, doubleEnabled, numDouble, numFreeze, numClick);
    }
}