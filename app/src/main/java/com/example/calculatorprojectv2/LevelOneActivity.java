package com.example.calculatorprojectv2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        args.putString("useNetworkEndFragment", "false");

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.calculator_fragment, CalculatorFragment.class, args)
                .commit();

//        CalculatorFragment.newInstance(points, doubleEnabled, numDouble, numFreeze, numClick)
//                .setArguments(points, doubleEnabled, numDouble, numFreeze, numClick);
    }

    public boolean useNormalEndScreen() {
        return true;
    }
}