package com.example.calculatorprojectv2;

// This is a data class intended to store information about each quest.
public class Quest {

    private static final int NUMBER_OPERATIONS = 4;

    private int buttonLimit;
    private int targetNumber;
    private int operationLimit;
    private int id;

    public int getButtonLimit() { return buttonLimit; }

    public int getTargetNumber() { return targetNumber; }

    public int getId() { return id; }

    public Quest(int id) {
        this.id = id;
        createTargets();
    }

    /**
     * @return The target parameters
     */
    public void createTargets() {
        // TODO: Create an algorithm to randomly create numbers
        operationLimit = randInt(1, NUMBER_OPERATIONS);
        switch (operationLimit) {
            case 1:
                // This is the case for addition
                // Limit is 9999
                int num1 = randInt(0, 499);
                int num2 = randInt(0, 499);
                targetNumber = num1 + num2;
                // This isn't the best code but it's definitely the easiest
                buttonLimit = String.valueOf(num1).length() + String.valueOf(num2).length();
                break;
            case 2:
                // This is the case for subtraction
                System.out.println(operationLimit);
                break;
            case 3:
                // This is the case for multiplication
                System.out.println(operationLimit);
                break;
            case 4:
                // This is the case for division
                System.out.println(operationLimit);
                break;
        }
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
}
