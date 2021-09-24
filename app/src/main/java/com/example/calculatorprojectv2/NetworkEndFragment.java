package com.example.calculatorprojectv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkEndFragment extends Fragment {

    private TextView currentScoreTxt, highScoreTxt, leaderTxt;
    private Button replayGame;
    private int numDoublePowerup = 0;
    private final boolean doublePointsEnabled = false;

    private View binding;

    /** ViewModel to communicate with fragment */
    private NetworkViewModel viewModel;

    public NetworkEndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_network_end, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(NetworkViewModel.class);

        currentScoreTxt = binding.findViewById(R.id.gameScoreLabel);
        highScoreTxt = binding.findViewById(R.id.highScoreLabel);
        leaderTxt = binding.findViewById(R.id.leaderLabel);

        initializeTextViews();

        viewModel.getCompanionLevels().observe(requireActivity(), level -> {
            int maxLevel = max(level);
            String s = "High Score: " + maxLevel*10;
            highScoreTxt.setText(s);
        });

        viewModel.getLeader().observe(requireActivity(), leader -> {
            String s = "Current Leader: " + leader;
            leaderTxt.setText(s);
        });

        viewModel.getPoints().observe(requireActivity(), points -> {
            String s = "Your Score: " + points;
            currentScoreTxt.setText(s);
        });

        //currentScoreTxt.setText("Your score ");

        replayGame = binding.findViewById(R.id.singlePlayerBtn);
        replayGame.setOnClickListener(view ->{
            Intent intent = new Intent(requireActivity(), GameTypeActivity.class);
            try{
                assert getArguments() != null;
                numDoublePowerup = Integer.parseInt(getArguments().getString("NumDouble"));
                int numFreezePowerup = Integer.parseInt(getArguments().getString("NumFreeze"));
                int numClickPowerup = Integer.parseInt(getArguments().getString("NumClick"));
                intent.putExtra("NumFreeze", numFreezePowerup+"");
                intent.putExtra("NumDouble", numDoublePowerup+"");
                intent.putExtra("NumClick", numClickPowerup+"");
                intent.putExtra("isDoublePointsEnabled", doublePointsEnabled);
                System.out.println("Doubled points powerup: " + numDoublePowerup);
                String numTotalPts = getArguments().getString("Points");
                intent.putExtra("Points", numTotalPts);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            startActivity(intent);
        });

        return binding;
    }

    public void initializeTextViews() {
        if(viewModel.getPoints().getValue()!=null) {
            String s = "Your Score: " + viewModel.getPoints().getValue();
            currentScoreTxt.setText(s);
        }

        if(viewModel.getCompanionLevels().getValue()!=null) {
            String s = "High Score: " + max(viewModel.getCompanionLevels().getValue()) * 10;
            highScoreTxt.setText(s);
        }

        if(viewModel.getLeader().getValue()!=null) {
            String s = "Current Leader: " + viewModel.getLeader().getValue();
            currentScoreTxt.setText(s);
        }
    }

    public int max(ArrayList<Integer> a) {
        if(a.size() == 0) return 0;
        int max = a.get(0);
        for (int i = 0; i < a.size(); i++) {
            if(a.get(i) > max) max = a.get(i);
        }
        return max;
    }
}