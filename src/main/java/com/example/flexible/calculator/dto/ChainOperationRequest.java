package com.example.flexible.calculator.dto;

import com.example.flexible.calculator.Operation;

/**
 * Request DTO for chain operation steps.
 */
public class ChainOperationRequest {
    private Operation operation;
    private Double operand;

    public ChainOperationRequest() {}

    public ChainOperationRequest(Operation operation, Double operand) {
        this.operation = operation;
        this.operand = operand;
    }

    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }

    public Double getOperand() { return operand; }
    public void setOperand(Double operand) { this.operand = operand; }
}
