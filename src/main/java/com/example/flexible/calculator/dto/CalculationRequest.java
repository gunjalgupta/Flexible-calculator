package com.example.flexible.calculator.dto;

import com.example.flexible.calculator.Operation;

/**
 * Request DTO for single calculation operations.
 */
public class CalculationRequest {
    private Operation operation;
    private Double num1;
    private Double num2;

    public CalculationRequest() {}

    public CalculationRequest(Operation operation, Double num1, Double num2) {
        this.operation = operation;
        this.num1 = num1;
        this.num2 = num2;
    }

    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }

    public Double getNum1() { return num1; }
    public void setNum1(Double num1) { this.num1 = num1; }

    public Double getNum2() { return num2; }
    public void setNum2(Double num2) { this.num2 = num2; }
}