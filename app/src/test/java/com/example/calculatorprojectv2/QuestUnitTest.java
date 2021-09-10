package com.example.calculatorprojectv2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class QuestUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    // These are all tests for the Quest object to make sure that it is working correctly.
    // Each one works by creating 10,000 Quest objects and validating each one

    @Test
    public void quest_buttonLimit_isWorking() {
        for (int i = 0; i < 10000; i++) {
            Quest q = new Quest(i);
            double[] testNums = q.getTestNums();
            // Checks to see if the length of each test number plus the length of the other test number plus one is the button limit
            // This is good for everything else
            assertEquals(String.valueOf(testNums[0]).length() + String.valueOf(testNums[1]).length() + 1,
                    q.getButtonLimit());
        }
    }

    @Test
    public void quest_targetNumber_isWorking() {
        double precision = 0.01;
        for (int i = 0; i < 10000; i++) {
            Quest q = new Quest(i);
            double[] testNums = q.getTestNums();
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
                    assertEquals(testNums[0] / testNums[1], q.getTargetNumber(), precision);
                    break;
            }
        }
    }

    @Test
    public void quest_operationDesignation_isWorking() {
        for (int i = 0; i < 10000; i++) {
            Quest q = new Quest(i);
            assertTrue(1 <= q.getOperationDesignation() && q.getOperationDesignation() <= Quest.NUMBER_OPERATIONS);
        }
    }

    @Test
    public void quest_numberLimit_isBelow() {
        for (int i = 0; i < 10000; i++) {
            Quest q = new Quest(i);
            double[] testNums = q.getTestNums();
            assertTrue(testNums[0] < 10000);
            assertTrue(testNums[1] < 10000);
            assertTrue(q.getTargetNumber() < 10000);
        }
    }
}