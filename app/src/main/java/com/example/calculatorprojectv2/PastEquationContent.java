package com.example.calculatorprojectv2;

public class PastEquationContent {
    private String equation;
    private String result;

    public PastEquationContent(String equation, String result) {
        this.equation = equation;
        this.result = result;
    }

    public String getEquation() {
        return equation;
    }

    public String getResult() {
        return result;
    }
}
