package com.example.calculatorprojectv2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.mariuszgromada.math.mxparser.Expression;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LevelOneActivity extends AppCompatActivity {
    TextView display, goalDisplay, buttonClickCounter, levelDisplay, operationDisplay; //add a TextView for the number that the use has to reach
    Button bAdd, bSub, bMulti, bDiv, bPwr, bClear, bDelete, bEnter;
    ProgressBar progressBar;
    private final Button[] numberButtons = new Button[10];

    private int clickCounter = 0;
    private String displayLabel = "";
    private int level = 1;

    // These are test goals that have the same parameters as the old ones
    private final Goal[] testGoals = {
            new Goal(3, 64, 3, false, 0),
            new Goal(4, 55, 3, false, 1),
            new Goal(5, 169, 3, false, 2),
            new Goal(5, 267, 1, true, 3),
            new Goal(3, 12, 3, false, 4)
    };
    private Goal activeGoal;
    private static final boolean USE_GOAL_TESTS = true;

    private final CharSequence keystrokeOver = "Too many Button Presses!";
    private final CharSequence sillyGoose = "I said Addition you silly goose :)";
    private Toast toast;
    private Toast fgd;
    private final CharSequence textOver = "Goal Overshot!";
    private final CharSequence textUnder = "Goal Undershot!";
    private Toast underShot;
    private Toast overShot;
    private final CharSequence textUseOperator = "Remember to use an operator!";
    private Toast useOperator;

    public static final String[] OPERATIONS_DISPLAY = new String[]{"+", "-", "×", "÷", "^"};

    private final ArrayList<PastEquationContent> pastEquationList = new ArrayList<>();
    private PastEquationAdapter pastEquationAdapter;
    private RecyclerView rvPastEquations;

    private Timer timer;
    private long seconds = 0;
    private static final int TIMER_PERIOD = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("button_" + i , "id", getPackageName());
            numberButtons[i] = findViewById(id);
        }
        //^^ The numerical calculator buttons

        bAdd = findViewById(R.id.additionButton);
        bSub = findViewById(R.id.subtractionButton);
        bMulti = findViewById(R.id.multiplicationButton);
        bDiv = findViewById(R.id.divisionButton);
        bPwr = findViewById(R.id.exponentButton);

        bClear = findViewById(R.id.clearButton);
        bDelete = findViewById(R.id.deleteButton);
        bEnter = findViewById(R.id.calculateButton);

        setButtonClickListeners();
        //^^ For the Click Listener for the Button

        display = findViewById(R.id.display);
        goalDisplay = findViewById((R.id.goalDisplay));
        buttonClickCounter = findViewById(R.id.clicksLeft);
        levelDisplay = findViewById(R.id.levelLabel);
        operationDisplay = findViewById(R.id.operationDisplay);
        //^^ Sets the Displays

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, keystrokeOver, duration);
        fgd = Toast.makeText(context, sillyGoose, duration);
        underShot = Toast.makeText(context, textUnder, duration);
        overShot = Toast.makeText(context, textOver, duration);
        useOperator = Toast.makeText(context, textUseOperator, duration);

        rvPastEquations = findViewById(R.id.operationList);
        pastEquationAdapter = new PastEquationAdapter(pastEquationList);
        rvPastEquations.setAdapter(pastEquationAdapter);
        rvPastEquations.setLayoutManager(new LinearLayoutManager(context));

        startGoalRuns();

        progressBar = findViewById(R.id.timerBar);
        progressBar.setMax(activeGoal.getTime());
        progressBar.setProgress(activeGoal.getTime());
        timer = new Timer();
        startTimer();
    }

    private final Runnable generate = () -> {
        progressBar.incrementProgressBy(-1 * TIMER_PERIOD);
        seconds += TIMER_PERIOD;
        if(seconds > activeGoal.getTime()) {
            finishScreen("You ran out of time!");
            timer.cancel();
        }
    };

    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                decreaseProgress();
            }
        }, 1, TIMER_PERIOD);
    }

    public void resetTimer() {
        timer.cancel();
        timer = new Timer();
        progressBar.setMax(activeGoal.getTime());
        progressBar.setProgress(activeGoal.getTime());
        seconds = 0;
        startTimer();
    }

    private void decreaseProgress(){
        this.runOnUiThread(generate);
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
        if (USE_GOAL_TESTS) {
            int lastIndex = activeGoal.getId();
            if (lastIndex + 1 >= testGoals.length) {
                Log.e("LEVEL_ONE_ACTIVITY", "Not enough goals in the list.");
            } else {
                activeGoal = testGoals[lastIndex + 1];
            }
        } else {
            activeGoal = new Goal(activeGoal.getId() + 1);
        }

        clickCounter = activeGoal.getButtonLimit();
        displayLabel = "";
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeGoal.getTargetNumber()));
        //constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeGoal.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks Left: %d", clickCounter));
        level++;
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));

        String operationLimit = "Operation Limit: ";
        if(activeGoal.isLimited()) {
            operationLimit += OPERATIONS_DISPLAY[activeGoal.getOperationDesignation() - 1];
        } else {
            operationLimit += "None";
        }
        operationDisplay.setText(operationLimit);
    }

    private void finishScreen(String message){
        addScoretoTextFile();
        displayLabel = message;
        display.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        display.setText(displayLabel);
        //constraintDisplay.setVisibility(View.GONE);
        goalDisplay.setVisibility(View.GONE);
        buttonClickCounter.setVisibility(View.GONE);
        levelDisplay.setVisibility(View.GONE);
        operationDisplay.setVisibility(View.GONE);

        for (Button b:numberButtons) {
            b.setVisibility(View.GONE);
        }

        bAdd.setVisibility(View.GONE);
        bSub.setVisibility(View.GONE);
        bMulti.setVisibility(View.GONE);
        bDiv.setVisibility(View.GONE);
        bPwr.setVisibility(View.GONE);
        bEnter.setVisibility(View.GONE);
        bClear.setVisibility(View.GONE);
        bDelete.setVisibility(View.GONE);

        rvPastEquations.setVisibility(View.GONE);

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void addScoretoTextFile() {
        String outFile = "src/main/res/user_scores.txt";
        try {
            PrintWriter out = new PrintWriter(outFile);
            out.println(level);
            out.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong!");
        }
    }

    public void updateOnButtonClick(String newEntry) {
        if (activeGoal.isLimited()) {
            if(!Arrays.asList(Goal.NUMBERS).contains(newEntry)) {
                if(!Goal.OPERATIONS[activeGoal.getOperationDesignation() - 1].equals(newEntry)) {
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
        // FIXME: This is for testing
        if (clickCounter < -100) {
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
            boolean hasOperators = false;

            // There is some time spent on using the × and ÷ so gotta do that too
            String expEval = pastExpEval.replaceAll("×", "*");
            expEval = expEval.replaceAll("÷", "/");

            //checks to make sure operator is used
            for(String operator: Goal.OPERATIONS){
                if (expEval.contains(operator)) {
                    hasOperators = true;
                    break;
                }
            }

            // This is actually an interesting idea. The Expression class auto-solves it for us
            // TODO: Replace the switch case in the Goal class with expEval
            Expression exp = new Expression(expEval);

            // FIXME: How should we deal with doubles and division it's currently not dealt with at all
            //double result = (int)(exp.calculate() * 10000)/10000.0;
            double result = exp.calculate();
            String resultS = String.valueOf(result);
            if(resultS.equals("NaN")) {
                // How should we deal with these? Right now I'm just setting it to 0 but that's incorrect
                result = 0;
                resultS = String.valueOf(result);
            }

            if(!hasOperators){
                useOperator.show();
                displayLabel = "";
                clickCounter = activeGoal.getButtonLimit();
            } else if (result == activeGoal.getTargetNumber()) {
                if (activeGoal.getId() == testGoals.length - 1) {
                    finishScreen("You beat the game!");
                    return;
                }
                resetTimer();
                runNextGoal();
                clearRVPastEquation();
            } else {
                // FIXME: This is changed so that the answer won't submit until they reach the result under the click limit
                //  They'll just keep on changing the number that they got from the last equation

                displayLabel = resultS;
                updateRVPastEquation(pastExpEval, resultS);

//                if (result > activeGoal.getTargetNumber()) {
//                    overShot.show();
//                } else {
//                    underShot.show();
//                }
//
//                displayLabel = "";
//                clickCounter = activeGoal.getButtonLimit();
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
}