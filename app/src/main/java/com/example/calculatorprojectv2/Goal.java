package com.example.calculatorprojectv2;

import androidx.annotation.NonNull;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
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
    // TODO: Implement changing times, currently set at 10,000 milliseconds
    private int time = 1000000000;

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
                int num1 = randInt(0, 4999);
                int num2 = randInt(0, 4999);
                testNums = new int[]{num1, num2};
                targetNumber = num1 + num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 2:
                // This is the case for subtraction
                num1 = randInt(0, 9999);
                num2 = randInt(0, 9999);
                testNums = new int[]{num1, num2};
                targetNumber = num1 - num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 3:
                // This is the case for multiplication
                num1 = randInt(0, 100);
                num2 = randInt(0, 99);
                testNums = new int[]{num1, num2};
                targetNumber = num1 * num2;
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length() + 1;
                break;
            case 4:
                // This is the case for division
                // Going backwards and generating a multiplication, then using the result and the first number
                num1 = randInt(1, 100);
                num2 = randInt(1, 99);
                testNums = new int[]{num1 * num2, num1};
                targetNumber = num2;
                buttonLimit = String.valueOf(num1 * num2).length() + String.valueOf(num1).length() + 1;
                break;
            case 5:
                // This is the case for exponents
                // Works by going backwards
                num1 = randInt(1, 100);
                num2 = randInt(1, 99);
                testNums = new int[]{num1 ^ num2, num1};
                targetNumber = num2;
                buttonLimit = String.valueOf(num1 ^ num2).length() + String.valueOf(num1).length() + 1;
                break;
        }

        isLimited = randInt(1, 1) == 1;

        // createTargetsMultiple();
    }


    /**
     * This is the number of entries that the goal will have.
     * For example, 12 + 6 + 2 has 5 entries.
     */
    private int numEntries = randInt(3, 7);
    private String[] randExpression;
    private boolean[] entryIsNumber;
    // We only want
    public static final String[] NUMBERS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static final String[] OPERATIONS = new String[]{"+", "-", "*", "/", "^"};

    public void createTargetsMultiple() {
        // TODO: Add support for larger numbers (two digit should be the limit but maybe score based)
        // The idea for this algorithm is that they enter in a multitude on 1 number operations

        numEntries = 20;
        isLimited = randInt(1, 5) == 1;

        // Start off with a number
        if((numEntries - 1) % 2 != 0) {
            numEntries--;
        }

        randExpression = new String[numEntries];
        entryIsNumber = new boolean[numEntries];

        boolean isNumber = true;
        for (int entryIndex = 0; entryIndex < numEntries; entryIndex++) {
            String[] stringToAdd = OPERATIONS;
            if(isNumber) stringToAdd = NUMBERS;

            // If we're dividing we gotta do something different
            String operation = stringToAdd[randInt(0, stringToAdd.length - 1)];
            if(operation.equals("/")) {
                // Get factors of the previous number
                int prevNumber = Integer.parseInt(randExpression[entryIndex - 1]);
                ArrayList<Integer> prevFactors = new ArrayList<>();
                // Add 1 just so that prime numbers don't break things
                prevFactors.add(1);
                for (int i = 2; i < Math.sqrt(prevNumber); i++) {
                    if(prevNumber % i == 0) {
                        prevFactors.add(i);
                    }
                }

                // If there's another factor I don't want to keep dividing by 1
                if(prevFactors.size() > 1) prevFactors.remove(0);

                // Set the next number to something random in those factors
                int nextNumber = prevFactors.get(randInt(0, prevFactors.size() - 1));

                // The current entry is set to the division operator (obviously)
                randExpression[entryIndex] = operation;
                entryIsNumber[entryIndex] = isNumber;

                // And the next entry is set to the nextNumber that we found earlier
                randExpression[entryIndex + 1] = String.valueOf(nextNumber);
                entryIsNumber[entryIndex + 1] = isNumber;

                // Then we skip the next entry (it's already been decided by the previous line)
                entryIndex++;
                continue;
            } else if(operation.equals("^")){
                if(randExpression[entryIndex - 2].equals("^")) {
                    operation = stringToAdd[randInt(0, stringToAdd.length - 1)];
                    randExpression[entryIndex] = operation;
                    entryIsNumber[entryIndex] = isNumber;
                    continue;
                }
                randExpression[entryIndex] = operation;
                entryIsNumber[entryIndex] = isNumber;

                int nextNumber = randInt(0, 3);
                randExpression[entryIndex + 1] = String.valueOf(nextNumber);
                entryIsNumber[entryIndex + 1] = isNumber;
                entryIndex++;
                continue;
            }

            randExpression[entryIndex] = operation;
            entryIsNumber[entryIndex] = isNumber;
            isNumber = !isNumber;
        }

        StringBuilder newExpression = new StringBuilder();
        for (String s : randExpression) {
            newExpression.append(s);
        }
        Expression e = new Expression(String.valueOf(newExpression));

        System.out.println(Arrays.toString(entryIsNumber));
        System.out.println(Arrays.toString(randExpression));
        System.out.println("Expression: " + newExpression);
        System.out.println(e.calculate());
        // targetNumber = (int) e.calculate();
        // buttonLimit = randExpression.length;
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
