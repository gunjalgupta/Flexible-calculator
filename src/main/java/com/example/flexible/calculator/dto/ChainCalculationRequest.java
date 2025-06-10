package com.example.flexible.calculator.dto;

import java.util.List;

/**
 * Request DTO for chained calculations.
 */
public class ChainCalculationRequest {
    private Double initialValue;
    private List<ChainOperationRequest> operations;

    public ChainCalculationRequest() {}

    public ChainCalculationRequest(Double initialValue, List<ChainOperationRequest> operations) {
        this.initialValue = initialValue;
        this.operations = operations;
    }

    public Double getInitialValue() { return initialValue; }
    public void setInitialValue(Double initialValue) { this.initialValue = initialValue; }

    public List<ChainOperationRequest> getOperations() { return operations; }
    public void setOperations(List<ChainOperationRequest> operations) { this.operations = operations; }
}
