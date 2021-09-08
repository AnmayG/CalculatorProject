package com.example.calculatorprojectv2;

// This is a data class intended to store information about each quest.
public class Quest {

    private int buttonLimit;
    private int targetNumber;
    private int id;

    public int getButtonLimit() { return buttonLimit; }

    public int getTargetNumber() { return targetNumber; }

    public int getId() { return id; }

    public Quest(int id) {
        int[] targetParameters = createTargetParameters();
        this.buttonLimit = targetParameters[0];
        this.targetNumber = targetParameters[1];
        this.id = id;
    }

    /**
     * @return Creates the target number and the number of button clicks
     */
    public int[] createTargetParameters() {
        return new int[]{0, 0};
    }

    /**
     * @return Creates the restriction and sets buttonLimit
     */
    public String createRestrictions() {

        return "";
    }
}
