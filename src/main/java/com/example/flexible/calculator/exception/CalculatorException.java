package com.example.flexible.calculator.exception;

/**
 * Custom exception for calculator-specific errors.
 */
public class CalculatorException extends RuntimeException {

    public CalculatorException(String message) {
        super(message);
    }

    public CalculatorException(String message, Throwable cause) {
        super(message, cause);
    }
}