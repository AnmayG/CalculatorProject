package com.example.calculatorprojectv2;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
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

    private final double goalOne = 64.0;
    private final double goalTwo = 55.0;
    private final double goalThree = 169.0;
    private final double goalFour = 267.0;
    private final double goalFive = 12.0;

    private boolean goalOneA = false;
    private boolean goalTwoA = false;
    private boolean goalThreeA = false;
    private boolean goalFourA = false;
    private boolean goalFiveA = false;

    private final int goalOneClick = 3;
    private final int goalTwoClick = 4;
    private final int goalThreeClick = 5;
    private final int goalFourClick = 6;
    private final int goalFiveClick = 3;

    // TODO: These are test quests that have the same parameters as the old ones
    private final Quest goalQuest1 = new Quest(3, 64, 3, false, 0);
    private final Quest goalQuest2 = new Quest(4, 55, 3, false, 1);
    private final Quest goalQuest3 = new Quest(5, 169, 3, false, 2);
    private final Quest goalQuest4 = new Quest(5, 267, 2, true, 3);
    private final Quest goalQuest5 = new Quest(3, 12, 3, false, 4);
    private Quest activeQuest;

    private Context context;
    private final CharSequence keystrokeOver = "Too many Button Presses!";
    private final CharSequence sillyGoose = "I said Addition you silly goose :)";
    private final int duration = Toast.LENGTH_SHORT;
    private Toast toast;
    private Toast fgd;
    private final CharSequence textOver = "Goal Overshot!";
    private final CharSequence textUnder = "Goal Undershot!";
    private Toast underShot;
    private Toast overShot;


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
        levelDisplay = findViewById(R.id.levelLabel);
        //^^ Sets the Displays

        context = getApplicationContext();

        toast = Toast.makeText(context, keystrokeOver, duration);
        fgd = Toast.makeText(context, sillyGoose, duration);
        underShot = Toast.makeText(context, textUnder, duration);
        overShot = Toast.makeText(context, textOver, duration);

        goalOneRun();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void goalOneRun(){
        activeQuest = goalQuest1;

        clickCounter = 0;
        goalDisplay.setText("Goal: " + goalOne);
        constraintDisplay.setText("Three Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
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
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalThree);
        constraintDisplay.setText("5 Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalFourRun(){
        activeQuest = goalQuest4;

        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalFour);
        constraintDisplay.setText("Use Addition with 5 Button Presses");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalFiveRun(){
        activeQuest = goalQuest5;

        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalFive);
        constraintDisplay.setText("3 Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void finishScreen(){
        display.setText("You Beat the Game!");
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
    }

    // TODO: Separate each button into its separate function so as to eliminate the switch case statement
    @Override
    public void onClick(View view) {
        // TODO: Add activeQuest field that acts as the current active quest
        // This is how we're going to add the endless Quest things
        switch (view.getId()){
            //TODO: ADD CASE STATEMENT for CASE 0;

            case R.id.buttonOne:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);

                // TODO: Figure out the displayLabel because that's a thing that needs algorithmic changes
                // right now it's just a string
                displayLabel = displayLabel.concat("1");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonTwo:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("2");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonThree:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("3");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonFour:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("4");
                display.setText(displayLabel);


                checkOverButtonLimit();
                break;
            case R.id.buttonFive:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("5");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonSix:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("6");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonSeven:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("7");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonEight:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("8");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.buttonNine:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("9");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.additionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("+");
                display.setText(displayLabel);


                checkOverButtonLimit();
                break;
            case R.id.subtractionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("-");
                display.setText(displayLabel);

                checkOverButtonLimit();
                break;
            case R.id.multiplicationButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("×");
                display.setText(displayLabel);


                checkOverButtonLimit();
                break;
            case R.id.divisionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("÷");
                display.setText(displayLabel);


                checkOverButtonLimit();
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
                    goalTwoRun();
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
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                break;
        }
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