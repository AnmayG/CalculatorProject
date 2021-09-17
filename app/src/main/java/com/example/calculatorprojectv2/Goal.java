package com.example.calculatorprojectv2;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

// This is a data class intended to store information about each goal.
public class Goal {

    // public static final int so that it can be accessed anywhere
    // probably bad code so make sure to double check it
    public static final int NUMBER_OPERATIONS = 5;

    private int buttonLimit;
    private int targetNumber;
    private int[] testNums;
    private int operationDesignation;
    private boolean isLimited;
    private final int id;

    // TODO: Implement changing times, currently set at 10,000 seconds should be 10,000 milliseconds
    private int time = 1000000000;

    private int randLimit = 10000;

    public int getButtonLimit() {
        return buttonLimit;
    }

    public double getTargetNumber() {
        return targetNumber;
    }

    public int getId() {
        return id;
    }

    public int getOperationDesignation() {
        return operationDesignation;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public int[] getTestNums() {
        return testNums;
    }

    public int getTime() {
        return time;
    }

    public Goal(int id) {
        this.id = id;
        createTargets();
    }

    public Goal(int buttonLimit, int targetNumber, int operationDesignation, boolean isLimited, int id) {
        this.buttonLimit = buttonLimit;
        this.targetNumber = targetNumber;
        this.operationDesignation = operationDesignation;
        this.isLimited = isLimited;
        this.id = id;
    }

    public void createTargets() {
        // TODO: Create an actual algorithm to randomly create numbers

        // Currently, the algorithm is extremely simplistic and does not have advanced calculations
        // It may be a good idea to incorporate high-level processes such as checking to make sure that the sum is 9999
        // Additionally, other operations need to be added manually with this design, which obviously isn't a good thing
        // This is a quick hack that is meant to be changed.
        // Limit is 9999
        // It also needs to be refined for multiple operations

        operationDesignation = randInt(1, NUMBER_OPERATIONS);
        switch (operationDesignation) {
            case 1:
                // This is the case for addition
                int num1 = randInt(0, randLimit / 2 - 1);
                int num2 = randInt(0, randLimit / 2 - 1);
                testNums = new int[]{num1, num2};
                targetNumber = num1 + num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 2:
                // This is the case for subtraction
                num1 = randInt(0, randLimit - 1);
                num2 = randInt(0, randLimit - 1);
                testNums = new int[]{num1, num2};
                targetNumber = num1 - num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 3:
                // This is the case for multiplication
                num1 = randInt(0, (randLimit / 10));
                num2 = randInt(0, (randLimit / 10) - 1);
                testNums = new int[]{num1, num2};
                targetNumber = num1 * num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 4:
                // This is the case for division
                // Going backwards and generating a multiplication, then using the result and the first number
                num1 = randInt(1, randLimit / 10);
                num2 = randInt(1, randLimit / 10 - 1);
                testNums = new int[]{num1 * num2, num1};
                targetNumber = num2;
                buttonLimit = String.valueOf(num1 * num2).length() + String.valueOf(num1).length() + 1;
                break;
            case 5:
                // This is the case for exponents
                // Works by going backwards
                num1 = randInt(1, randLimit - 1);
                num2 = randInt(1, randLimit / 10 - 1);
                testNums = new int[]{(int) Math.sqrt(num1), num1};
                targetNumber = num2;
                buttonLimit = String.valueOf((int) Math.pow(num1, num2)).length() + String.valueOf(num1).length() + 1;
                break;
        }

        isLimited = randInt(1, 1) == 1;
    }


    /**
     * Generates a random number between min and max, inclusive.
     * @param min The minimum range for the random number
     * @param max The maximum range for the random number
     * @return A random number between min and max, inclusive
     */
    public static int randInt(int min, int max){
        return (int)(Math.random()*(max + 1 - min)) + min;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Goal %d " +
                        "\n  Button Limit: %d" +
                        "\n  Target Number: %d" +
                        "\n  Operation Designation: %d" +
                        "\n  Is Limited: %b" +
                        "\n  Test Numbers: %s",
                id, buttonLimit, targetNumber, operationDesignation, isLimited, Arrays.toString(testNums));
    }
}
