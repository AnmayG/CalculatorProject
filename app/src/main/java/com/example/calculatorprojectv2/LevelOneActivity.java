package com.example.calculatorprojectv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.mariuszgromada.math.mxparser.*;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class LevelOneActivity extends AppCompatActivity implements View.OnClickListener{
    TextView display, goalDisplay, buttonClickCounter, constraintDisplay, levelDisplay; //add a TextView for the number that the use has to reach
    Button bOne, bTwo, bThree, bFour, bFive, bSix, bSeven, bEight, bNine, bAdd, bSub, bMulti, bDiv, cButton;

    private int clickCounter = 0;
    private String displayLabel = "";
    private int level = 1;

    private double goalOne = 64.0;
    private double goalTwo = 55.0;
    private double goalThree = 169.0;
    private double goalFour = 267.0;
    private double goalFive = 12.0;

    private boolean goalOneA = false;
    private boolean goalTwoA = false;
    private boolean goalThreeA = false;
    private boolean goalFourA = false;
    private boolean goalFiveA = false;

    private int goalOneClick = 3;
    private int goalTwoClick = 4;
    private int goalThreeClick = 5;
    private int goalFourClick = 6;
    private int goalFiveClick = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        bOne = (Button) findViewById(R.id.buttonOne);
        bTwo = (Button) findViewById(R.id.buttonTwo);
        bThree = (Button) findViewById(R.id.buttonThree);
        bFour = (Button) findViewById(R.id.buttonFour);
        bFive = (Button) findViewById(R.id.buttonFive);
        bSix = (Button) findViewById(R.id.buttonSix);
        bSeven = (Button) findViewById(R.id.buttonSeven);
        bEight = (Button) findViewById(R.id.buttonEight);
        bNine = (Button) findViewById(R.id.buttonNine);
        bAdd = (Button) findViewById(R.id.additionButton);
        bSub = (Button) findViewById(R.id.subtractionButton);
        bMulti = (Button) findViewById(R.id.multiplicationButton);
        bDiv = (Button) findViewById(R.id.divisionButton);
        cButton = (Button) findViewById(R.id.calculateButton);
        //^^ The numerical calculator buttons

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
        cButton.setOnClickListener(this);
        //^^ For the Click Listener for the Button

        display = (TextView) findViewById(R.id.display);
        goalDisplay = (TextView) findViewById((R.id.goalDisplay));
        buttonClickCounter = (TextView) findViewById(R.id.buttonClickCounter);
        constraintDisplay = (TextView) findViewById(R.id.constraintDisplay);
        levelDisplay = (TextView) findViewById(R.id.levelLabel);
        //^^ Sets the Displays

        goalOneRun();
    }

    private void goalOneRun(){
        clickCounter = 0;
        goalDisplay.setText("Goal: " + goalOne);
        constraintDisplay.setText("Three Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
    }

    private void goalTwoRun(){
        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalTwo);
        constraintDisplay.setText("Four Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalThreeRun(){
        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalThree);
        constraintDisplay.setText("5 Buttons");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalFourRun(){
        clickCounter = 0;
        displayLabel = "";
        goalDisplay.setText("Goal: " + goalFour);
        constraintDisplay.setText("Use Addition with 5 Button Presses");
        buttonClickCounter.setText("Button Clicks: " + clickCounter);
        level++;
        levelDisplay.setText("Level: " + level);
    }

    private void goalFiveRun(){
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

    @Override
    public void onClick(View view) {
        Context context = getApplicationContext();
        CharSequence keystrokeOver = "Too many Button Presses!";
        CharSequence sillyGoose = "I said Addition you silly goose :)";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, keystrokeOver, duration);
        Toast fgd = Toast.makeText(context, sillyGoose, duration);

        Context contextTwo = getApplicationContext();
        CharSequence textOver = "Goal Overshot!";
        CharSequence textUnder = "Goal Undershot!";
        int durationTwo = Toast.LENGTH_SHORT;

        Toast underShot = Toast.makeText(contextTwo, textUnder, durationTwo);
        Toast overShot = Toast.makeText(contextTwo, textOver, durationTwo);

        switch (view.getId()){
            case R.id.buttonOne:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("1");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonTwo:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("2");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonThree:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("3");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonFour:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("4");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonFive:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("5");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonSix:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("6");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonSeven:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("7");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonEight:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("8");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.buttonNine:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("9");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.additionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("+");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    if (clickCounter > goalFourClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.subtractionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("-");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    fgd.show();
                    displayLabel = "";
                    display.setText(displayLabel);
                    clickCounter = 0;
                    buttonClickCounter.setText("Button Clicks: " + clickCounter);
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.multiplicationButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("×");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    fgd.show();
                    displayLabel = "";
                    display.setText(displayLabel);
                    clickCounter = 0;
                    buttonClickCounter.setText("Button Clicks: " + clickCounter);
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.divisionButton:
                clickCounter++;
                buttonClickCounter.setText("Button Clicks: " + clickCounter);
                displayLabel = displayLabel.concat("÷");
                display.setText(displayLabel);

                if (!goalOneA){
                    if (clickCounter > goalOneClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalTwoA){
                    if (clickCounter > goalTwoClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalThreeA){
                    if (clickCounter > goalThreeClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                } else if (!goalFourA){
                    fgd.show();
                    displayLabel = "";
                    display.setText(displayLabel);
                    clickCounter = 0;
                    buttonClickCounter.setText("Button Clicks: " + clickCounter);
                } else if (!goalFiveA){
                    if (clickCounter > goalFiveClick){
                        toast.show();
                        displayLabel = "";
                        display.setText(displayLabel);
                        clickCounter = 0;
                        buttonClickCounter.setText("Button Clicks: " + clickCounter);
                    }
                }

                break;
            case R.id.calculateButton:
                String expEval = display.getText().toString();

                expEval = expEval.replaceAll("×", "*");
                expEval = expEval.replaceAll("÷", "/");

                Expression exp = new Expression(expEval);

                String resultS = String.valueOf(exp.calculate());
                double result = Double.parseDouble(resultS);

                if (!goalOneA){
                    if (result == goalOne){
                        goalOneA = true;
                        goalTwoRun();
                    } else {
                        if (result > goalOne){
                            overShot.show();
                        } else {
                            underShot.show();
                        }

                        displayLabel = "";
                        clickCounter = 0;
                    }

                    display.setText(displayLabel);
                    buttonClickCounter.setText("Button Clicks: " + clickCounter);
                } else if (!goalTwoA){
                    if (result == goalTwo){
                        goalTwoA = true;
                        goalThreeRun();
                    } else {
                        if (result > goalTwo){
                            overShot.show();
                        } else {
                            underShot.show();
                        }

                        displayLabel = "";
                        clickCounter = 0;
                    }

                    display.setText(displayLabel);
                    buttonClickCounter.setText("Button clicks: " + clickCounter);
                } else if (!goalThreeA){
                    if (result == goalThree){
                        goalThreeA = true;
                        goalFourRun();
                    } else {
                        if (result > goalThree){
                            overShot.show();
                        } else {
                            underShot.show();
                        }

                        displayLabel = "";
                        clickCounter = 0;
                    }

                    display.setText(displayLabel);
                    buttonClickCounter.setText("Button clicks: " + clickCounter);
                } else if (!goalFourA){
                    if (result == goalFour){
                        goalFourA = true;
                        goalFiveRun();
                    } else {
                        if (result > goalFour){
                            overShot.show();
                        } else {
                            underShot.show();
                        }

                        displayLabel = "";
                        clickCounter = 0;
                    }

                    display.setText(displayLabel);
                    buttonClickCounter.setText("Button Clicks: " + clickCounter);
                } else if (!goalFiveA){
                    if (result == goalFive){
                        goalFiveA = true;
                        finishScreen();
                    } else {
                        if (result > goalFive){
                            overShot.show();
                        } else {
                            underShot.show();
                        }
                    }
                }

                break;
        }
    }
}
