package com.example.calculatorprojectv2;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PrizeFragment extends Fragment {

    public static PrizeFragment newInstance() {
        return new PrizeFragment();
    }
    private Button freezePower, doublePower, clickPower, exit;

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.prize_fragment, container, false);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.prize_fragment);
//        freezePower = findViewById(R.id.freezeTimer);
//
//    }

}