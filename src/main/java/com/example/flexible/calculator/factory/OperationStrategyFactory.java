package com.example.flexible.calculator.factory;

import com.example.flexible.calculator.Operation;
import com.example.flexible.calculator.strategy.OperationStrategy;
import com.example.flexible.calculator.strategy.impl.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Factory for creating operation strategy instances.
 * This class can be extended to support new operations without modifying the Calculator.
 */
public class OperationStrategyFactory {
    private final Map<Operation, OperationStrategy> strategies;

    public OperationStrategyFactory() {
        strategies = new HashMap<>();
        initializeStrategies();
    }

    /**
     * Constructor for dependency injection support.
     *
     * @param strategies pre-configured map of operation strategies
     */
    public OperationStrategyFactory(Map<Operation, OperationStrategy> strategies) {
        this.strategies = new HashMap<>(strategies);
    }

    private void initializeStrategies() {
        strategies.put(Operation.ADD, new AdditionStrategy());
        strategies.put(Operation.SUBTRACT, new SubtractionStrategy());
        strategies.put(Operation.MULTIPLY, new MultiplicationStrategy());
        strategies.put(Operation.DIVIDE, new DivisionStrategy());
    }

    /**
     * Gets the appropriate strategy for the given operation.
     *
     * @param operation the operation to get strategy for
     * @return the operation strategy
     * @throws UnsupportedOperationException if the operation is not supported
     */
    public OperationStrategy getStrategy(Operation operation) {
        OperationStrategy strategy = strategies.get(operation);
        if (strategy == null) {
            throw new UnsupportedOperationException("Operation " + operation + " is not supported");
        }
        return strategy;
    }

    /**
     * Registers a new operation strategy.
     * Allows runtime extension of supported operations.
     *
     * @param operation the operation to register
     * @param strategy the strategy implementation
     */
    public void registerStrategy(Operation operation, OperationStrategy strategy) {
        if (operation == null || strategy == null) {
            throw new IllegalArgumentException("Operation and strategy cannot be null");
        }
        strategies.put(operation, strategy);
    }
}