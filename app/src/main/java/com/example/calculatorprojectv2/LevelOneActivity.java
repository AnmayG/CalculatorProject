package com.example.calculatorprojectv2;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.Arrays;
import java.util.Locale;

public class LevelOneActivity extends AppCompatActivity {
    TextView display, goalDisplay, buttonClickCounter, levelDisplay, constraintDisplay, operationDisplay; //add a TextView for the number that the use has to reach
    Button bAdd, bSub, bMulti, bDiv, bPwr, bClear, bDelete, bEnter;
    private final Button[] numberButtons = new Button[10];

    private int clickCounter = 0;
    private String displayLabel = "";
    private int level = 1;

    // TODO: These are test quests that have the same parameters as the old ones
    private final Quest[] goalQuests = {
            new Quest(3, 64, 3, false, 0),
            new Quest(4, 55, 3, false, 1),
            new Quest(5, 169, 3, false, 2),
            new Quest(5, 267, 1, true, 3),
            new Quest(3, 12, 3, false, 4)
    };
    private Quest activeQuest;
    private static final boolean USE_GOAL_TESTS = true;
    private static final String REWARD_MESSAGE = "You beat the game!";

    private final CharSequence keystrokeOver = "Too many Button Presses!";
    private final CharSequence sillyGoose = "I said Addition you silly goose :)";
    private Toast toast;
    private Toast fgd;
    private final CharSequence textOver = "Goal Overshot!";
    private final CharSequence textUnder = "Goal Undershot!";
    private Toast underShot;
    private Toast overShot;

    public static final String[] OPERATIONS_DISPLAY = new String[]{"+", "-", "×", "÷", "^"};

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
        constraintDisplay = findViewById(R.id.constraintDisplay);
        operationDisplay = findViewById(R.id.operationDisplay);
        //^^ Sets the Displays

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, keystrokeOver, duration);
        fgd = Toast.makeText(context, sillyGoose, duration);
        underShot = Toast.makeText(context, textUnder, duration);
        overShot = Toast.makeText(context, textOver, duration);

        startGoalRuns();
    }

    private void startGoalRuns(){
        if(activeQuest == null) {
            if(USE_GOAL_TESTS) {
                activeQuest = goalQuests[0];
            } else {
                activeQuest = new Quest(0);
            }
        }

        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeQuest.getTargetNumber()));
        constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeQuest.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));

        String operationLimit = "Operation Limit: ";
        if(activeQuest.isLimited()) {
            operationLimit += OPERATIONS_DISPLAY[activeQuest.getOperationDesignation() - 1];
        } else {
            operationLimit += "None";
        }
        operationDisplay.setText(operationLimit);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    //^^DO WE NEED THIS? (onCreateView)

    private void runNextGoal() {
        if (USE_GOAL_TESTS) {
            int lastIndex = activeQuest.getId();
            if (lastIndex + 1 >= goalQuests.length) {
                Log.e("LEVEL_ONE_ACTIVITY", "Not enough quests in the list.");
            } else {
                activeQuest = goalQuests[lastIndex + 1];
            }
        } else {
            activeQuest = new Quest(activeQuest.getId() + 1);
        }

        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeQuest.getTargetNumber()));
        constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeQuest.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        level++;
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));

        String operationLimit = "Operation Limit: ";
        if(activeQuest.isLimited()) {
            operationLimit += OPERATIONS_DISPLAY[activeQuest.getOperationDesignation() - 1];
        } else {
            operationLimit += "None";
        }
        operationDisplay.setText(operationLimit);
    }

    private void finishScreen(){
        displayLabel = REWARD_MESSAGE;
        display.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        display.setText(displayLabel);
        constraintDisplay.setVisibility(View.GONE);
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
    }

    public void updateOnButtonClick(String newEntry) {
        if (activeQuest.isLimited()) {
            if(!Arrays.asList(Quest.NUMBERS).contains(newEntry)) {
                if(!Quest.OPERATIONS_ACTUAL[activeQuest.getOperationDesignation() - 1].equals(newEntry)) {
                    fgd.show();
                    return;
                }
            }
        }

        clickCounter++;
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        displayLabel = displayLabel.concat(newEntry);
        display.setText(displayLabel);

        checkOverButtonLimit();
    }

    public void checkOverButtonLimit() {
        if (clickCounter > activeQuest.getButtonLimit()) {
            toast.show();
            displayLabel = "";
            display.setText(displayLabel);
            clickCounter = 0;
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        }
    }

    //on click listeners for each button
    private void setButtonClickListeners() {
        for (int i = 0; i < numberButtons.length; i++) {
            int finalI = i;
            numberButtons[i].setOnClickListener(view -> updateOnButtonClick(String.valueOf(finalI)));
        }

        bDelete.setOnClickListener(view -> {
            clickCounter--;
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
            displayLabel = displayLabel.substring(0, displayLabel.length() - 1);
            display.setText(displayLabel);
        });

        bClear.setOnClickListener(view -> {
            clickCounter = 0;
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
            displayLabel = "";
            display.setText(displayLabel);
        });

        bEnter.setOnClickListener(view -> {
            String expEval = display.getText().toString();

            // There is some time spent on using the × and ÷ so gotta do that too
            expEval = expEval.replaceAll("×", "*");
            expEval = expEval.replaceAll("÷", "/");

            // This is actually an interesting idea. The Expression class auto-solves it for us
            // TODO: Replace the switch case in the Quest class with expEval
            Expression exp = new Expression(expEval);

            String resultS = String.valueOf(exp.calculate());
            double result = Double.parseDouble(resultS);

            if (result == activeQuest.getTargetNumber()) {
                if (activeQuest.getId() == 4) {
                    finishScreen();
                    return;
                }
                runNextGoal();
            } else {
                if (result > activeQuest.getTargetNumber()) {
                    overShot.show();
                } else {
                    underShot.show();
                }

                displayLabel = "";
                clickCounter = 0;
            }

            display.setText(displayLabel);
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        });

        bAdd.setOnClickListener(view -> updateOnButtonClick("+"));

        bSub.setOnClickListener(view -> updateOnButtonClick("-"));

        bMulti.setOnClickListener(view -> updateOnButtonClick("×"));

        bDiv.setOnClickListener(view -> updateOnButtonClick("÷"));

        bPwr.setOnClickListener(view -> updateOnButtonClick("^"));
    }
}