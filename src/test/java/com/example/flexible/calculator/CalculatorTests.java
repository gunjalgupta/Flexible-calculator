package com.example.flexible.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Calculator class.
 * Tests both normal operations and edge cases.
 */
class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Basic addition operation")
    void testAddition() {
        double result = calculator.calculate(Operation.ADD, Integer.valueOf(5), Integer.valueOf(3));
        assertEquals(8.0, result, 0.001);
    }

    @Test
    @DisplayName("Basic subtraction operation")
    void testSubtraction() {
        double result = calculator.calculate(Operation.SUBTRACT, Integer.valueOf(10), Integer.valueOf(4));
        assertEquals(6.0, result, 0.001);
    }

    @Test
    @DisplayName("Basic multiplication operation")
    void testMultiplication() {
        double result = calculator.calculate(Operation.MULTIPLY, Integer.valueOf(6), Integer.valueOf(7));
        assertEquals(42.0, result, 0.001);
    }

    @Test
    @DisplayName("Basic division operation")
    void testDivision() {
        double result = calculator.calculate(Operation.DIVIDE, Integer.valueOf(15), Integer.valueOf(3));
        assertEquals(5.0, result, 0.001);
    }

    @Test
    @DisplayName("Division by zero throws ArithmeticException")
    void testDivisionByZero() {
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculator.calculate(Operation.DIVIDE, Integer.valueOf(10), Integer.valueOf(0))
        );
        assertEquals("Division by zero is not allowed", exception.getMessage());
    }

    @Test
    @DisplayName("Null operation throws IllegalArgumentException")
    void testNullOperation() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(null, Integer.valueOf(5), Integer.valueOf(3))
        );
        assertEquals("Operation cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Null numbers throw IllegalArgumentException")
    void testNullNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(Operation.ADD, null, Integer.valueOf(3)));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(Operation.ADD, Integer.valueOf(5), null));
    }

    @Test
    @DisplayName("Operations with decimal numbers")
    void testDecimalOperations() {
        double result1 = calculator.calculate(Operation.ADD, Double.valueOf(2.5), Double.valueOf(3.7));
        assertEquals(6.2, result1, 0.001);

        double result2 = calculator.calculate(Operation.DIVIDE, Double.valueOf(7.5), Double.valueOf(2.5));
        assertEquals(3.0, result2, 0.001);
    }

    @Test
    @DisplayName("Operations with different Number types")
    void testDifferentNumberTypes() {
        double result1 = calculator.calculate(Operation.ADD, Integer.valueOf(5), 3.5f);
        assertEquals(8.5, result1, 0.001);

        double result2 = calculator.calculate(Operation.MULTIPLY, 2L, Integer.valueOf(4));
        assertEquals(8.0, result2, 0.001);
    }

    @Test
    @DisplayName("Simple chaining operations")
    void testChaining() {
        double result = calculator.startChain(Integer.valueOf(10))
                .apply(Operation.ADD, Integer.valueOf(5))
                .apply(Operation.MULTIPLY, Integer.valueOf(2))
                .apply(Operation.SUBTRACT, Integer.valueOf(3))
                .getResult();

        assertEquals(27.0, result, 0.001); // ((10 + 5) * 2) - 3 = 27
    }

    @Test
    @DisplayName("Complex chaining operations")
    void testComplexChaining() {
        double result = calculator.startChain(Integer.valueOf(100))
                .apply(Operation.DIVIDE, Integer.valueOf(4))    // 25
                .apply(Operation.SUBTRACT, Integer.valueOf(5))  // 20
                .apply(Operation.MULTIPLY, Integer.valueOf(3))  // 60
                .apply(Operation.ADD, Integer.valueOf(15))      // 75
                .getResult();

        assertEquals(75.0, result, 0.001);
    }

    @Test
    @DisplayName("Chaining with reset")
    void testChainingWithReset() {
        Calculator.ChainCalculator chain = calculator.startChain(Integer.valueOf(5))
                .apply(Operation.ADD, Integer.valueOf(3))
                .reset(Integer.valueOf(10))
                .apply(Operation.MULTIPLY, Integer.valueOf(2));

        assertEquals(20.0, chain.getResult(), 0.001);
    }

    @Test
    @DisplayName("Chaining with division by zero")
    void testChainingDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.startChain(Integer.valueOf(10))
                    .apply(Operation.ADD, Integer.valueOf(5))
                    .apply(Operation.DIVIDE, Integer.valueOf(0));
        });
    }

    @Test
    @DisplayName("Null initial value for chaining")
    void testNullInitialValue() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.startChain(null));
    }

    @Test
    @DisplayName("Null operand in chaining")
    void testNullOperandInChaining() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.startChain(Integer.valueOf(5))
                    .apply(Operation.ADD, null);
        });
    }

    @Test
    @DisplayName("Null value in reset")
    void testNullValueInReset() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.startChain(Integer.valueOf(5))
                    .reset(null);
        });
    }

    @Test
    @DisplayName("Large number operations")
    void testLargeNumbers() {
        double result = calculator.calculate(Operation.ADD,
                Double.MAX_VALUE / 2, Double.MAX_VALUE / 2);
        assertTrue(Double.isFinite(result));
    }

    @Test
    @DisplayName("Negative number operations")
    void testNegativeNumbers() {
        double result1 = calculator.calculate(Operation.ADD, Integer.valueOf(-5), Integer.valueOf(-3));
        assertEquals(-8.0, result1, 0.001);

        double result2 = calculator.calculate(Operation.MULTIPLY, Integer.valueOf(-4), Integer.valueOf(3));
        assertEquals(-12.0, result2, 0.001);

        double result3 = calculator.calculate(Operation.DIVIDE, Integer.valueOf(-10), Integer.valueOf(-2));
        assertEquals(5.0, result3, 0.001);
    }
}
