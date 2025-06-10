package com.example.flexible.calculator;

import com.example.flexible.calculator.factory.OperationStrategyFactory;
import com.example.flexible.calculator.strategy.OperationStrategy;

/**
 * Main Calculator class that supports basic operations and chaining.
 * Follows the Open-Closed Principle - open for extension, closed for modification.
 * Compatible with IoC containers for dependency injection.
 */
public class Calculator {
    private final OperationStrategyFactory strategyFactory;

    /**
     * Default constructor - creates factory with default strategies.
     */
    public Calculator() {
        this.strategyFactory = new OperationStrategyFactory();
    }

    /**
     * Constructor for dependency injection.
     *
     * @param strategyFactory the factory for operation strategies
     */
    public Calculator(OperationStrategyFactory strategyFactory) {
        if (strategyFactory == null) {
            throw new IllegalArgumentException("OperationStrategyFactory cannot be null");
        }
        this.strategyFactory = strategyFactory;
    }

    /**
     * Performs a single calculation between two numbers.
     *
     * @param operation the operation to perform
     * @param num1 the first number
     * @param num2 the second number
     * @return the result of the calculation
     * @throws UnsupportedOperationException if the operation is not supported
     * @throws ArithmeticException if the operation is mathematically invalid
     * @throws IllegalArgumentException if any parameter is null
     */
    public double calculate(Operation operation, Number num1, Number num2) {
        validateInputs(operation, num1, num2);

        OperationStrategy strategy = strategyFactory.getStrategy(operation);
        return strategy.execute(num1.doubleValue(), num2.doubleValue());
    }

    /**
     * Creates a new chaining calculator starting with the given initial value.
     *
     * @param initialValue the starting value for calculations
     * @return a new ChainCalculator instance
     */
    public ChainCalculator startChain(Number initialValue) {
        if (initialValue == null) {
            throw new IllegalArgumentException("Initial value cannot be null");
        }
        return new ChainCalculator(this, initialValue.doubleValue());
    }

    private void validateInputs(Operation operation, Number num1, Number num2) {
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }
        if (num1 == null || num2 == null) {
            throw new IllegalArgumentException("Numbers cannot be null");
        }
    }

    /**
     * Inner class for chaining operations.
     * Provides a fluent interface for sequential calculations.
     */
    public static class ChainCalculator {
        private final Calculator calculator;
        private double currentValue;

        private ChainCalculator(Calculator calculator, double initialValue) {
            this.calculator = calculator;
            this.currentValue = initialValue;
        }

        /**
         * Applies an operation with the given operand to the current value.
         *
         * @param operation the operation to perform
         * @param operand the operand for the operation
         * @return this ChainCalculator for method chaining
         * @throws UnsupportedOperationException if the operation is not supported
         * @throws ArithmeticException if the operation is mathematically invalid
         */
        public ChainCalculator apply(Operation operation, Number operand) {
            currentValue = calculator.calculate(operation, currentValue, operand);
            return this;
        }

        /**
         * Gets the current result of the chained calculations.
         *
         * @return the current calculated value
         */
        public double getResult() {
            return currentValue;
        }

        /**
         * Resets the calculator to a new value.
         *
         * @param value the new value to start from
         * @return this ChainCalculator for method chaining
         */
        public ChainCalculator reset(Number value) {
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
            this.currentValue = value.doubleValue();
            return this;
        }
    }
}

