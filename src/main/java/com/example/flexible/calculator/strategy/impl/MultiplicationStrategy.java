package com.example.flexible.calculator.strategy.impl;

import com.example.flexible.calculator.strategy.OperationStrategy;

/**
 * Strategy implementation for multiplication operation.
 */
public class MultiplicationStrategy implements OperationStrategy {
    @Override
    public double execute(double operand1, double operand2) {
        return operand1 * operand2;
    }
}