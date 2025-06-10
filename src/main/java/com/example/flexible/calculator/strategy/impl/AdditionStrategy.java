package com.example.flexible.calculator.strategy.impl;

import com.example.flexible.calculator.strategy.OperationStrategy;

/**
 * Strategy implementation for addition operation.
 */
public class AdditionStrategy implements OperationStrategy {
    @Override
    public double execute(double operand1, double operand2) {
        return operand1 + operand2;
    }
}
