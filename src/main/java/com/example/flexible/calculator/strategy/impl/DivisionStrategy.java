package com.example.flexible.calculator.strategy.impl;

import com.example.flexible.calculator.strategy.OperationStrategy;

/**
 * Strategy implementation for division operation.
 */
public class DivisionStrategy implements OperationStrategy {
    @Override
    public double execute(double operand1, double operand2) throws ArithmeticException {
        if (operand2 == 0.0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return operand1 / operand2;
    }
}