package com.example.flexible.calculator.strategy.impl;

import com.example.flexible.calculator.strategy.OperationStrategy;

/**
 * Strategy implementation for subtraction operation.
 */
public class SubtractionStrategy implements OperationStrategy {
    @Override
    public double execute(double operand1, double operand2) {
        return operand1 - operand2;
    }
}
