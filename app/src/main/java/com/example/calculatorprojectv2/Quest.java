package com.example.calculatorprojectv2;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

// This is a data class intended to store information about each quest.
public class Quest {

    // public static final int so that it can be accessed anywhere
    // probably bad code so make sure to double check it
    public static final int NUMBER_OPERATIONS = 4;

    private int buttonLimit;
    private double targetNumber;
    private double[] testNums;
    private int operationDesignation;
    private boolean isLimited;
    private int id;

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

    public double[] getTestNums() {
        return testNums;
    }

    public Quest(int id) {
        this.id = id;
        createTargets();
    }

    public Quest(int buttonLimit, int targetNumber, int operationDesignation, boolean isLimited, int id) {
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

        operationDesignation = randInt(1, NUMBER_OPERATIONS);
        switch (operationDesignation) {
            case 1:
                // This is the case for addition
                double num1 = randInt(0, 4999);
                double num2 = randInt(0, 4999);
                testNums = new double[]{num1, num2};
                targetNumber = num1 + num2;
                // This isn't the best code but it's definitely the easiest
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 2:
                // This is the case for subtraction
                num1 = randInt(0, 9999);
                num2 = randInt(0, 9999);
                testNums = new double[]{num1, num2};
                targetNumber = num1 - num2;
                // This isn't the best code but it's definitely the easiest
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 3:
                // This is the case for multiplication
                num1 = randInt(0, 100);
                num2 = randInt(0, 99);
                testNums = new double[]{num1, num2};
                targetNumber = num1 * num2;
                // This isn't the best code but it's definitely the easiest
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 4:
                // This is the case for division
                num1 = randInt(0, 9999);
                num2 = randInt(1, 100);
                testNums = new double[]{num1, num2};
                targetNumber = num1 / num2;
                // This isn't the best code but it's definitely the easiest
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
        }

        isLimited = randInt(0, 1) == 1;
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
        return String.format(Locale.getDefault(), "Quest %d " +
                        "\n  Button Limit: %d" +
                        "\n  Target Number: %d" +
                        "\n  Operation Designation: %d" +
                        "\n  Is Limited: %b" +
                        "\n  Test Numbers: %s",
                id, buttonLimit, targetNumber, operationDesignation, isLimited, Arrays.toString(testNums));
    }
}
