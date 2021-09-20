package com.example.calculatorprojectv2;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class GoalUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    // These are all tests for the Goal object to make sure that it is working correctly.
    // Each one works by creating 10,000 Goal objects and validating each one

    @Test
    public void goal_buttonLimit_isWorking() {
        Goal q = new Goal(0);
        for (int i = 0; i < q.getRandLimit(); i++) {
            q = new Goal(i);
            int[] testNums = q.getTestNums();
            // Checks to see if the length of each test number plus the length of the other test number plus one is the button limit
            // This is good for everything else
            // System.out.println("BUTTON_LIMIT \n" + q.toString() + "\n" + testNums[0] + " " + testNums[1] + " " + q.getButtonLimit());
            assertEquals(String.valueOf(testNums[0]).length() + String.valueOf(testNums[1]).length() + 1,
                    q.getButtonLimit());
        }
    }

    @Test
    public void goal_targetNumber_isWorking() {
        Goal q = new Goal(0);
        double precision = 0.01;
        for (int i = 0; i < q.getRandLimit(); i++) {
            q = new Goal(i);
            int[] testNums = q.getTestNums();
            switch (q.getOperationDesignation()) {
                case 1:
                    assertEquals(testNums[0] + testNums[1], q.getTargetNumber(), precision);
                    break;
                case 2:
                    assertEquals(testNums[0] - testNums[1], q.getTargetNumber(), precision);
                    break;
                case 3:
                    assertEquals(testNums[0] * testNums[1], q.getTargetNumber(), precision);
                    break;
                case 4:
                    assertEquals(testNums[0] / ((double) testNums[1]), q.getTargetNumber(), precision);
                    break;
            }
        }
    }

    @Test
    public void goal_operationDesignation_isWorking() {
        Goal q = new Goal(0);
        for (int i = 0; i < q.getRandLimit(); i++) {
            q = new Goal(i);
            assertTrue(1 <= q.getOperationDesignation() && q.getOperationDesignation() <= Goal.NUMBER_OPERATIONS);
        }
    }

    @Test
    public void goal_numberLimit_isBelow() {
        Goal q = new Goal(0);
        for (int i = 0; i < q.getRandLimit(); i++) {
            q = new Goal(i);
            int[] testNums = q.getTestNums();
            // System.out.println("NUMBER_LIMIT \n" + q.toString() + "\n" + testNums[0] + " " + testNums[1] + " " + q.getButtonLimit());
            assertTrue(testNums[0] < q.getRandLimit());
            assertTrue(testNums[1] < q.getRandLimit());
            assertTrue(q.getTargetNumber() < q.getRandLimit());
        }
    }
}