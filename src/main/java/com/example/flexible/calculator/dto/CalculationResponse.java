package com.example.flexible.calculator.dto;
/**
 * Response DTO for calculation results.
 */
public class CalculationResponse {
    private Double result;
    private String error;
    private boolean success;

    public CalculationResponse() {}

    public CalculationResponse(Double result) {
        this.result = result;
        this.success = true;
    }

    public CalculationResponse(String error) {
        this.error = error;
        this.success = false;
    }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}

