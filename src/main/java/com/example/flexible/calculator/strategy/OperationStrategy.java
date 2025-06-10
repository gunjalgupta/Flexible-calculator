package com.example.flexible.calculator.strategy;

/**
 * Strategy interface for calculator operations.
 * Enables the Open-Closed Principle by allowing new operations
 * to be added without modifying existing code.
 */
public interface OperationStrategy {
    /**
     * Performs the operation on two operands.
     *
     * @param operand1 the first operand
     * @param operand2 the second operand
     * @return the result of the operation
     * @throws ArithmeticException if the operation is invalid (e.g., division by zero)
     */
    double execute(double operand1, double operand2) throws ArithmeticException;
}