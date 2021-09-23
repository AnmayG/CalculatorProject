package com.example.calculatorprojectv2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {

    TextView display, goalDisplay, buttonClickCounter, levelDisplay, operationDisplay; //add a TextView for the number that the use has to reach
    Button bAdd, bSub, bMulti, bDiv, bPwr, bClear, bDelete, bEnter;
    ImageView click, freeze;
    ProgressBar progressBar;
    private final Button[] numberButtons = new Button[10];

    private int clickCounter = 0;
    private String displayLabel = "";
    private int level = 1;
    private boolean isDoublePointsEnabled = false;
    private int numDoublePowerup = 0;
    private int numFreezePowerup = 0;
    private int numClickPowerup = 0;
    private boolean timerPause = false;
    private boolean useNetworkEndFragment = false;

    private int points = 0;

    // These are test goals that have the same parameters as the old ones
    private final Goal[] testGoals = {
            new Goal(3, 64, 3, false, 0),
            new Goal(4, 55, 3, false, 1),
            new Goal(5, 169, 3, false, 2),
            new Goal(5, 267, 1, true, 3),
            new Goal(3, 12, 3, false, 4)
    };
    private Goal activeGoal;
    private static final boolean USE_GOAL_TESTS = false;

    private final CharSequence keystrokeOver = "Too many Button Presses!";
    private final CharSequence sillyGoose = "Use the right operator you silly goose :)";
    private Toast toast;
    private Toast fgd;
    private final CharSequence textUseOperator = "Remember to use an operator!";
    private Toast useOperator;

    public static final String[] OPERATIONS_DISPLAY = new String[]{"+", "-", "×", "÷", "^"};

    private final ArrayList<PastEquationContent> pastEquationList = new ArrayList<>();
    private PastEquationAdapter pastEquationAdapter;
    private RecyclerView rvPastEquations;

    private Timer timer;
    private long seconds = 0;
    private static final int TIMER_PERIOD = 20;

    private View binding;

    private NetworkViewModel viewModel;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            points = Integer.parseInt(getArguments().getString("Points"));
            isDoublePointsEnabled = Boolean.parseBoolean(getArguments().getString("isDoublePointsEnabled"));
            numDoublePowerup = Integer.parseInt(getArguments().getString("NumDouble"));
            System.out.println("double points: " + isDoublePointsEnabled + " " + numDoublePowerup);
            numFreezePowerup = Integer.parseInt(getArguments().getString("NumFreeze"));
            numClickPowerup = Integer.parseInt(getArguments().getString("NumClick"));
            useNetworkEndFragment = Boolean.parseBoolean(getArguments().getString("useNetworkEndFragment"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_level, container, false);

        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("button_" + i , "id", requireActivity().getPackageName());
            numberButtons[i] = binding.findViewById(id);
        }
        //^^ The numerical calculator buttons

        bAdd = binding.findViewById(R.id.additionButton);
        bSub = binding.findViewById(R.id.subtractionButton);
        bMulti = binding.findViewById(R.id.multiplicationButton);
        bDiv = binding.findViewById(R.id.divisionButton);
        bPwr = binding.findViewById(R.id.exponentButton);

        bClear = binding.findViewById(R.id.clearButton);
        bDelete = binding.findViewById(R.id.deleteButton);
        bEnter = binding.findViewById(R.id.calculateButton);

        click = binding.findViewById(R.id.clickImage);
        freeze = binding.findViewById(R.id.freezeImage);

        if(numClickPowerup > 0){
            click.setAlpha(1F);
        }
        if(numFreezePowerup > 0){
            freeze.setAlpha(1F);
        }

        setButtonClickListeners();
        //^^ For the Click Listener for the Button
        setPowerupClickListeners();

        display = binding.findViewById(R.id.display);
        goalDisplay = binding.findViewById((R.id.goalDisplay));
        buttonClickCounter = binding.findViewById(R.id.clicksLeft);
        levelDisplay = binding.findViewById(R.id.levelLabel);
        operationDisplay = binding.findViewById(R.id.operationDisplay);
        //^^ Sets the Displays

        Context context = requireActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, keystrokeOver, duration);
        fgd = Toast.makeText(context, sillyGoose, duration);
        /*
            We're doing the equation system so don't need this
            underShot = Toast.makeText(context, textUnder, duration);
            overShot = Toast.makeText(context, textOver, duration);
         */
        useOperator = Toast.makeText(context, textUseOperator, duration);

        rvPastEquations = binding.findViewById(R.id.operationList);
        pastEquationAdapter = new PastEquationAdapter(pastEquationList);
        rvPastEquations.setAdapter(pastEquationAdapter);
        rvPastEquations.setLayoutManager(new LinearLayoutManager(context));

        startGoalRuns();

        progressBar = binding.findViewById(R.id.timerBar);
        progressBar.setMax(activeGoal.getTime());
        progressBar.setProgress(activeGoal.getTime());
        timer = new Timer();
        seconds = 0;
        startTimer();

        viewModel = new ViewModelProvider(requireActivity()).get(NetworkViewModel.class);

        return binding;
    }

    private void setPowerupClickListeners() {
        click.setOnClickListener(view -> {
            if(numClickPowerup > 0){
                numClickPowerup -= 1;
                clickCounter++;
                buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
                if(numClickPowerup == 0){
                    click.setAlpha(0.5F);
                }
            }
        });

        freeze.setOnClickListener(view ->{
            if(numFreezePowerup > 0){
                numFreezePowerup--;
                //NEEDS TO ADD FREEZING TIMER OPTION HERE
                timerPause = true;

                if(numFreezePowerup == 0){
                    freeze.setAlpha(0.5F);
                }
            }
        });
    }

    private final Runnable generate = () -> progressBar.incrementProgressBy(-1 * TIMER_PERIOD);

    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!timerPause){
                    //System.out.println(this + " " + timer + " " + seconds + " " + progressBar.getProgress() + " " + activeGoal.getTime() + " " + activeGoal);
                    decreaseProgress();
                    seconds += TIMER_PERIOD;
                    if(seconds > activeGoal.getTime()) {
                        //System.out.println("CANCELLED " + seconds + " " + activeGoal + " " + TIMER_PERIOD);
                        finishScreen();
                        timer.cancel();
                    }
                }
            }
        }, 1, TIMER_PERIOD);
    }

    public void resetTimer() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        progressBar.setMax(activeGoal.getTime());
        progressBar.setProgress(activeGoal.getTime());
        seconds = 0;
        startTimer();
    }

    private void decreaseProgress(){
        requireActivity().runOnUiThread(generate);
    }

    @Override
    public void onStop() {
        timer.cancel();
        timer.purge();
        super.onStop();
    }

    private void startGoalRuns(){
        if(activeGoal == null) {
            if(USE_GOAL_TESTS) {
                activeGoal = testGoals[0];
            } else {
                activeGoal = new Goal(0);
            }
        }

        clickCounter = activeGoal.getButtonLimit();
        displayLabel = "";
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeGoal.getTargetNumber()));
        //constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeGoal.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));

        String operationLimit = "Operation Limit: ";
        if(activeGoal.isLimited()) {
            operationLimit += OPERATIONS_DISPLAY[activeGoal.getOperationDesignation() - 1];
        } else {
            operationLimit += "None";
        }
        operationDisplay.setText(operationLimit);
    }

    private void runNextGoal() {
        timerPause = false;
        if (USE_GOAL_TESTS) {
            int lastIndex = activeGoal.getId();
            if (lastIndex + 1 >= testGoals.length) {
                Log.e("LEVEL_ONE_ACTIVITY", "Not enough goals in the list.");
            } else {
                activeGoal = testGoals[lastIndex + 1];
            }
        } else {
            activeGoal = new Goal(activeGoal.getId() + 1);
            // System.out.println(activeGoal);
        }

        clickCounter = activeGoal.getButtonLimit();
        displayLabel = "";
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeGoal.getTargetNumber()));
        //constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeGoal.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
        level++;
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));

        if(useNetworkEndFragment) {
            viewModel.setCurrentLevel(level);
            System.out.println("LEVEL:" + viewModel.getCurrentLevel().getValue());
        }

        String operationLimit = "Operation Limit: ";
        if(activeGoal.isLimited()) {
            operationLimit += OPERATIONS_DISPLAY[activeGoal.getOperationDesignation() - 1];
        } else {
            operationLimit += "None";
        }
        operationDisplay.setText(operationLimit);
    }

    private void finishScreen() {
        int pointsAdded = level * 10;
        if(isDoublePointsEnabled){
            pointsAdded *= 2;
        }
        addScoreToDatabase(pointsAdded);
        points += pointsAdded;
        requireActivity().runOnUiThread(() -> viewModel.setPoints(points));

        if(useNetworkEndFragment) {
            ((NetworkActivity) requireActivity()).switchToEndFragment();
            return;
        }

        Intent intent = new Intent(requireActivity(), EndScreen.class);
        intent.putExtra("Points", points + "");
        intent.putExtra("PointsAdded", pointsAdded + "");
        intent.putExtra("NumFreeze", numFreezePowerup + "");
        intent.putExtra("NumDouble", numDoublePowerup + "");
        intent.putExtra("NumClick", numClickPowerup + "");
        startActivity(intent);
    }

    private void addScoreToDatabase(int points) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);

                try{
                    assert value != null;
                    if(points > Integer.parseInt(value)){
                        myRef.setValue(points+"");
                    }
                }
                catch(Exception ignored){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void updateOnButtonClick(String newEntry) {
        if (activeGoal.isLimited()) {
            if(isOperation(newEntry)) {
                if(!OPERATIONS_DISPLAY[activeGoal.getOperationDesignation() - 1].equals(newEntry)) {
                    fgd.show();
                    return;
                }
            }
        }

        clickCounter--;
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
        displayLabel = displayLabel.concat(newEntry);
        display.setText(displayLabel);

        checkOverButtonLimit();
    }

    public void checkOverButtonLimit() {
        // FIXME: This is for testing, switch -100 to 0
        if (clickCounter < 0) {
            toast.show();
            displayLabel = "";
            display.setText(displayLabel);
            clickCounter = activeGoal.getButtonLimit();
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
            clearRVPastEquation();
        }
    }

    //on click listeners for each button
    private void setButtonClickListeners() {
        for (int i = 0; i < numberButtons.length; i++) {
            int finalI = i;
            numberButtons[i].setOnClickListener(view -> updateOnButtonClick(String.valueOf(finalI)));
        }

        bDelete.setOnClickListener(view -> {
            clickCounter++;
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
            displayLabel = displayLabel.substring(0, displayLabel.length() - 1);
            display.setText(displayLabel);
        });

        bClear.setOnClickListener(view -> {
            clickCounter = activeGoal.getButtonLimit();
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
            displayLabel = "";
            display.setText(displayLabel);
        });

        bEnter.setOnClickListener(view -> {
            String pastExpEval = display.getText().toString();

            //checks to make sure operator is used
            boolean hasOperators = hasOperation(pastExpEval);

            // There is some time spent on using the × and ÷ so gotta do that too
            String expEval = pastExpEval.replaceAll("×", "*");
            expEval = expEval.replaceAll("÷", "/");

            // This is actually an interesting idea. The Expression class auto-solves it for us
            Expression exp = new Expression(expEval);

            //double result = (int)(exp.calculate() * 10000)/10000.0;
            double result = exp.calculate();
            String resultS = String.valueOf(result);
            if(resultS.equals("NaN")) {
                // How should we deal with these? Right now I'm just setting it to 0 but that's incorrect
                result = 0;
                resultS = String.valueOf(result);
            }

            // Checks for if there's a beginning (and as such invalid) operation
            if(!pastExpEval.equals("") && isOperation(String.valueOf(pastExpEval.charAt(0)))) {
                if (!String.valueOf(pastExpEval.charAt(0)).equals("-")) {
                    Toast.makeText(requireActivity().getApplicationContext(),
                            "Invalid operator", Toast.LENGTH_SHORT).show();
                    displayLabel = "";
                    clickCounter = activeGoal.getButtonLimit();
                    display.setText(displayLabel);
                    buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
                    return;
                }
            }

            if(!hasOperators) {
                useOperator.show();
                displayLabel = "";
                clickCounter = activeGoal.getButtonLimit();
            } else if(zeroChange(pastExpEval, resultS)) {
                Toast.makeText(requireActivity().getApplicationContext(),
                        "You can't use the result in the equation!", Toast.LENGTH_SHORT).show();
                displayLabel = "";
                clickCounter = activeGoal.getButtonLimit();
            } else if (result == activeGoal.getTargetNumber()) {
                if (activeGoal.getId() == testGoals.length - 1 && USE_GOAL_TESTS) {
                    finishScreen();
                    return;
                }
                runNextGoal();
                resetTimer();
                updateRVPastEquation(pastExpEval, resultS);
            } else {
                // FIXME: This is changed so that the answer won't submit until they reach the result under the click limit
                //  They'll just keep on changing the number that they got from the last equation

                displayLabel = resultS;
                updateRVPastEquation(pastExpEval, resultS);
            }

            display.setText(displayLabel);
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
        });

        bAdd.setOnClickListener(view -> updateOnButtonClick("+"));

        bSub.setOnClickListener(view -> updateOnButtonClick("-"));

        bMulti.setOnClickListener(view -> updateOnButtonClick("×"));

        bDiv.setOnClickListener(view -> updateOnButtonClick("÷"));

        bPwr.setOnClickListener(view -> updateOnButtonClick("^"));
    }

    private void updateRVPastEquation(String pastExpEval, String resultS) {
        pastEquationList.add(new PastEquationContent(pastExpEval, resultS));
        pastEquationAdapter.notifyItemInserted(pastEquationList.size() - 1);
        rvPastEquations.scrollToPosition(pastEquationAdapter.getItemCount() - 1);
    }

    private void clearRVPastEquation() {
        int size = pastEquationList.size();
        pastEquationList.clear();
        pastEquationAdapter.notifyItemRangeRemoved(0, size);
        rvPastEquations.scrollToPosition(0);
    }

    private boolean zeroChange(String input, String result) {
        boolean output = false;
        ArrayList<String> entries = splitInputIntoEntries(input);
        System.out.println(entries + " " + result);
        for (int i = 0; i < entries.size(); i++) {
            if(!isOperation(entries.get(i))) {
                if (Double.parseDouble(entries.get(i)) == Double.parseDouble(result)) {
                    output = true;
                    break;
                }
            }
        }
        return output;
    }

    public static ArrayList<String> splitInputIntoEntries(String input) {
        ArrayList<String> entries = new ArrayList<>();
        entries.add("");
        int entryIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            String character = String.valueOf(input.charAt(i));
            boolean isOperator = false;
            if(hasOperation(character) && !(character.equals("-") && i == 0)) {
                isOperator = true;
                entries.add("");
                entryIndex++;
            }
            entries.set(entryIndex, entries.get(entryIndex) + character);
            if(isOperator){
                entries.add("");
                entryIndex++;
            }
            // System.out.println(input + " " + entries);
        }
        if (entries.get(entries.size() - 1).equals("")) {
            entries.remove(entries.size() - 1);
        }
        return entries;
    }

    private static boolean isOperation(String i) {
        for (String s:OPERATIONS_DISPLAY) {
            if(i.equals(s)) return true;
        }
        return false;
    }

    private static boolean hasOperation(String i){
        for (String s:OPERATIONS_DISPLAY) {
            if(i.contains(s)) return true;
        }
        return false;
    }
}