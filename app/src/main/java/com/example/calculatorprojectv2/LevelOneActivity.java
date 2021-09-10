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

import java.util.Locale;

public class LevelOneActivity extends AppCompatActivity implements View.OnClickListener{
    TextView display, goalDisplay, buttonClickCounter, constraintDisplay, levelDisplay; //add a TextView for the number that the use has to reach
    Button bZero, bOne, bTwo, bThree, bFour, bFive, bSix, bSeven, bEight, bNine, bAdd, bSub, bMulti, bDiv, bPwr, bClear, bDelete, bEnter;

    private int clickCounter = 0;
    private String displayLabel = "";
    private int level = 1;

    // TODO: These are test quests that have the same parameters as the old ones
    private final Quest[] goalQuests = {
            new Quest(3, 64, 3, false, 0),
            new Quest(4, 55, 3, false, 1),
            new Quest(5, 169, 3, false, 2),
            new Quest(5, 267, 2, true, 3),
            new Quest(3, 12, 3, false, 4)
    };
    private Quest activeQuest;
    private static final boolean USE_GOAL_TESTS = false;

    private final CharSequence keystrokeOver = "Too many Button Presses!";
    private final CharSequence sillyGoose = "I said Addition you silly goose :)";
    private Toast toast;
    private Toast fgd;
    private final CharSequence textOver = "Goal Overshot!";
    private final CharSequence textUnder = "Goal Undershot!";
    private Toast underShot;
    private Toast overShot;

    private String rewardMessage = "You beat the game!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        bZero = findViewById(R.id.buttonZero);
        bOne = findViewById(R.id.buttonOne);
        bTwo = findViewById(R.id.buttonTwo);
        bThree = findViewById(R.id.buttonThree);
        bFour = findViewById(R.id.buttonFour);
        bFive = findViewById(R.id.buttonFive);
        bSix = findViewById(R.id.buttonSix);
        bSeven = findViewById(R.id.buttonSeven);
        bEight = findViewById(R.id.buttonEight);
        bNine = findViewById(R.id.buttonNine);
        bAdd = findViewById(R.id.additionButton);
        bSub = findViewById(R.id.subtractionButton);
        bMulti = findViewById(R.id.multiplicationButton);
        bDiv = findViewById(R.id.divisionButton);
        bPwr = findViewById(R.id.exponentButton);
        bClear = findViewById(R.id.clearButton);
        bDelete = findViewById(R.id.deleteButton);
        bEnter = findViewById(R.id.calculateButton);
        //^^ The numerical calculator buttons

        bZero.setOnClickListener(this);
        bOne.setOnClickListener(this);
        bTwo.setOnClickListener(this);
        bThree.setOnClickListener(this);
        bFour.setOnClickListener(this);
        bFive.setOnClickListener(this);
        bSix.setOnClickListener(this);
        bSeven.setOnClickListener(this);
        bEight.setOnClickListener(this);
        bNine.setOnClickListener(this);
        bAdd.setOnClickListener(this);
        bSub.setOnClickListener(this);
        bMulti.setOnClickListener(this);
        bDiv.setOnClickListener(this);
        bPwr.setOnClickListener(this);
        bClear.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        bEnter.setOnClickListener(this);
        //^^ For the Click Listener for the Button

        clickButton();

        display = findViewById(R.id.display);
        goalDisplay = findViewById((R.id.goalDisplay));
        buttonClickCounter = findViewById(R.id.buttonClickCounter);
        constraintDisplay = findViewById(R.id.constraintDisplay);
        levelDisplay = findViewById(R.id.levelLabel);
        //^^ Sets the Displays

        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, keystrokeOver, duration);
        fgd = Toast.makeText(context, sillyGoose, duration);
        underShot = Toast.makeText(context, textUnder, duration);
        overShot = Toast.makeText(context, textOver, duration);

        startGoalRuns();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void runNextGoal() {
        if(USE_GOAL_TESTS) {
            int lastIndex = activeQuest.getId();
            if(lastIndex > goalQuests.length) {
                Log.e("LEVEL_ONE_ACTIVITY", "Not enough quests in the list.");
            } else {
                activeQuest = goalQuests[lastIndex + 1];
            }
        } else {
            activeQuest = new Quest(activeQuest.getId() + 1);
        }

    private void goalTwoRun(){
        activeQuest = goalQuest2;

        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalTwo);
        constraintDisplay.setText("Four Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalThreeRun(){
        activeQuest = goalQuest3;

        clickCounter = 0;
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeQuest.getTargetNumber()));
        constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeQuest.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        level++;
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));
    }

    private void startGoalRuns(){
        if(activeQuest == null) {
            if(USE_GOAL_TESTS) {
                activeQuest = goalQuests[0];
            } else {
                activeQuest = new Quest(0);
            }
        }

        displayLabel = "";
        clickCounter = 0;
        goalDisplay.setText(String.format(Locale.getDefault(), "Goal: %s", activeQuest.getTargetNumber()));
        constraintDisplay.setText(String.format(Locale.getDefault(), "%d Buttons Allowed", activeQuest.getButtonLimit()));
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        level++;
        levelDisplay.setText(String.format(Locale.getDefault(), "Level: %d", level));
    }

    private void finishScreen(){
        display.setText(rewardMessage);
        constraintDisplay.setVisibility(View.GONE);
        goalDisplay.setVisibility(View.GONE);
        buttonClickCounter.setVisibility(View.GONE);
        levelDisplay.setVisibility(View.GONE);

        bZero.setVisibility(View.GONE);
        bOne.setVisibility(View.GONE);
        bTwo.setVisibility(View.GONE);
        bThree.setVisibility(View.GONE);
        bFour.setVisibility(View.GONE);
        bFive.setVisibility(View.GONE);
        bSix.setVisibility(View.GONE);
        bSeven.setVisibility(View.GONE);
        bEight.setVisibility(View.GONE);
        bNine.setVisibility(View.GONE);
        bAdd.setVisibility(View.GONE);
        bSub.setVisibility(View.GONE);
        bMulti.setVisibility(View.GONE);
        bDiv.setVisibility(View.GONE);
        cButton.setVisibility(View.GONE);
    }

    // TODO: Separate each button into its separate function so as to eliminate the switch case statement
    @Override
    public void onClick(View view) {
        // TODO: Add activeQuest field that acts as the current active quest
        // This is how we're going to add the endless Quest things
        switch (view.getId()){
            //TODO: ADD CASE STATEMENT for CASE 0;
            case R.id.buttonZero:
                updateOnButtonClick("0");
                break;
            case R.id.buttonOne:
                updateOnButtonClick("1");
                break;
            case R.id.buttonTwo:
                updateOnButtonClick("2");
                break;
            case R.id.buttonThree:
                updateOnButtonClick("3");
                break;
            case R.id.buttonFour:
                updateOnButtonClick("4");
                break;
            case R.id.buttonFive:
                updateOnButtonClick("5");
                break;
            case R.id.buttonSix:
                updateOnButtonClick("6");
                break;
            case R.id.buttonSeven:
                updateOnButtonClick("7");
                break;
            case R.id.buttonEight:
                updateOnButtonClick("8");
                break;
            case R.id.buttonNine:
                updateOnButtonClick("9");
                break;
            case R.id.additionButton:
                updateOnButtonClick("+");
                break;
            case R.id.subtractionButton:
                updateOnButtonClick("-");
                break;
            case R.id.multiplicationButton:
                updateOnButtonClick("×");
                break;
            case R.id.divisionButton:
                updateOnButtonClick("÷");
                break;
            case R.id.calculateButton:
                String expEval = display.getText().toString();

                // There is some time spent on using the × and ÷ so gotta do that too
                expEval = expEval.replaceAll("×", "*");
                expEval = expEval.replaceAll("÷", "/");

                // This is actually an interesting idea. The Expression class autosolves it for us
                // TODO: Replace the switch case in the Quest class with expEval
                Expression exp = new Expression(expEval);

                String resultS = String.valueOf(exp.calculate());
                double result = Double.parseDouble(resultS);

                if(result == activeQuest.getTargetNumber()) {
                    runNextGoal();
                    if (activeQuest.getId() == 5) {
                        finishScreen();
                    }
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
                return;
        }
        checkOverButtonLimit();
    }

    public void updateOnButtonClick(String addition) {
        if(activeQuest.isLimited()) {

        }

        clickCounter++;
        buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        displayLabel = displayLabel.concat(addition);
        display.setText(displayLabel);
    }

    public void checkOverButtonLimit() {
        if(clickCounter > activeQuest.getButtonLimit()) {
            toast.show();
            displayLabel = "";
            display.setText(displayLabel);
            clickCounter = 0;
            buttonClickCounter.setText(String.format(Locale.getDefault(), "Button Clicks: %d", clickCounter));
        }
    }

    //on click listeners for each button
    private void clickButton(){
        bZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bPwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}