package com.example.flexible.calculator.factory;


import com.example.flexible.calculator.Operation;
import com.example.flexible.calculator.strategy.OperationStrategy;
import com.example.flexible.calculator.strategy.impl.AdditionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OperationStrategyFactory.
 */
class OperationStrategyFactoryTest {
    private OperationStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new OperationStrategyFactory();
    }

    @Test
    @DisplayName("Factory returns correct strategies for all operations")
    void testGetAllStrategies() {
        assertNotNull(factory.getStrategy(Operation.ADD));
        assertNotNull(factory.getStrategy(Operation.SUBTRACT));
        assertNotNull(factory.getStrategy(Operation.MULTIPLY));
        assertNotNull(factory.getStrategy(Operation.DIVIDE));
    }

    @Test
    @DisplayName("Factory throws exception for null operation")
    void testNullOperation() {
        assertThrows(UnsupportedOperationException.class,
                () -> factory.getStrategy(null));
    }

    @Test
    @DisplayName("Register new strategy")
    void testRegisterStrategy() {
        // This would work if we had a new operation enum value
        OperationStrategy mockStrategy = (a, b) -> a % b;

        // Since we can't add enum values in tests, we test the null validation
        assertThrows(IllegalArgumentException.class,
                () -> factory.registerStrategy(null, mockStrategy));
        assertThrows(IllegalArgumentException.class,
                () -> factory.registerStrategy(Operation.ADD, null));
    }
}
