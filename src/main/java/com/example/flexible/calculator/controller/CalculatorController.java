// CalculatorController.java

package com.example.flexible.calculator.controller;

import com.example.flexible.calculator.Calculator;
import com.example.flexible.calculator.Operation;
import com.example.flexible.calculator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * REST Controller for calculator operations.
 * Provides HTTP endpoints for single and chained calculations.
 */
@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private final Calculator calculator;

    @Autowired
    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    /**
     * Performs a single calculation operation.
     *
     * POST /api/calculator/calculate
     * {
     *   "operation": "ADD",
     *   "num1": 5.0,
     *   "num2": 3.0
     * }
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest request) {
        try {
            validateCalculationRequest(request);

            double result = calculator.calculate(request.getOperation(), request.getNum1(), request.getNum2());
            return ResponseEntity.ok(new CalculationResponse(Double.valueOf(result)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Invalid input: " + e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Math error: " + e.getMessage()));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Unsupported operation: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CalculationResponse("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Performs chained calculations.
     *
     * POST /api/calculator/chain
     * {
     *   "initialValue": 10.0,
     *   "operations": [
     *     {"operation": "ADD", "operand": 5.0},
     *     {"operation": "MULTIPLY", "operand": 2.0}
     *   ]
     * }
     */
    @PostMapping("/chain")
    public ResponseEntity<CalculationResponse> calculateChain(@RequestBody ChainCalculationRequest request) {
        try {
            validateChainRequest(request);

            Calculator.ChainCalculator chain = calculator.startChain(request.getInitialValue());

            for (ChainOperationRequest operation : request.getOperations()) {
                chain.apply(operation.getOperation(), operation.getOperand());
            }

            double result = chain.getResult();
            return ResponseEntity.ok(new CalculationResponse(Double.valueOf(result)));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Invalid input: " + e.getMessage()));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Math error: " + e.getMessage()));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse("Unsupported operation: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CalculationResponse("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Gets all supported operations.
     *
     * GET /api/calculator/operations
     */
        @GetMapping("/operations")
    public ResponseEntity<List<Operation>> getSupportedOperations() {
        return ResponseEntity.ok(Arrays.asList(Operation.values()));
    }

    /**
     * Health check endpoint.
     *
     * GET /api/calculator/health
     */
        @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Calculator service is running");
    }

    private void validateCalculationRequest(CalculationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getOperation() == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }
        if (request.getNum1() == null || request.getNum2() == null) {
            throw new IllegalArgumentException("Numbers cannot be null");
        }
    }

    private void validateChainRequest(ChainCalculationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getInitialValue() == null) {
            throw new IllegalArgumentException("Initial value cannot be null");
        }
        if (request.getOperations() == null || request.getOperations().isEmpty()) {
            throw new IllegalArgumentException("Operations list cannot be null or empty");
        }

        for (ChainOperationRequest operation : request.getOperations()) {
            if (operation.getOperation() == null) {
                throw new IllegalArgumentException("Operation cannot be null");
            }
            if (operation.getOperand() == null) {
                throw new IllegalArgumentException("Operand cannot be null");
            }
        }
    }
}