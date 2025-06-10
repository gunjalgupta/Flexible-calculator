package com.example.flexible.calculator.controller;

import com.example.flexible.calculator.Calculator;
import com.example.flexible.calculator.Operation;
import com.example.flexible.calculator.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private Calculator calculator;

    @Mock
    private Calculator.ChainCalculator chainCalculator;

    @InjectMocks
    private CalculatorController calculatorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();
        objectMapper = new ObjectMapper();
    }

    // ========== Single Calculation Tests ==========

    @Test
    void calculate_ValidRequest_ReturnsSuccessResponse() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.ADD);
        request.setNum1(Double.valueOf(5.0));
        request.setNum2(Double.valueOf(3.0));

        when(calculator.calculate(Operation.ADD, 5.0, 3.0)).thenReturn(8.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8.0))
                .andExpect(jsonPath("$.error").doesNotExist());

        verify(calculator).calculate(Operation.ADD, 5.0, 3.0);
    }

    @Test
    void calculate_SubtractionOperation_ReturnsCorrectResult() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.SUBTRACT);
        request.setNum1(10.0);
        request.setNum2(4.0);

        when(calculator.calculate(Operation.SUBTRACT, 10.0, 4.0)).thenReturn(6.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(6.0));
    }

    @Test
    void calculate_MultiplicationOperation_ReturnsCorrectResult() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.MULTIPLY);
        request.setNum1(7.0);
        request.setNum2(3.0);

        when(calculator.calculate(Operation.MULTIPLY, 7.0, 3.0)).thenReturn(21.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(21.0));
    }

    @Test
    void calculate_DivisionOperation_ReturnsCorrectResult() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.DIVIDE);
        request.setNum1(Double.valueOf(15.0));
        request.setNum2(Double.valueOf(3.0));

        when(calculator.calculate(Operation.DIVIDE, 15.0, 3.0)).thenReturn(5.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5.0));
    }

    @Test
    void calculate_NullOperation_ReturnsBadRequest() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(null);
        request.setNum1(Double.valueOf(5.0));
        request.setNum2(Double.valueOf(3.0));

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Operation cannot be null"));
    }

    @Test
    void calculate_NullNumbers_ReturnsBadRequest() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.ADD);
        request.setNum1(null);
        request.setNum2(3.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Numbers cannot be null"));
    }

    @Test
    void calculate_DivisionByZero_ReturnsBadRequest() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.DIVIDE);
        request.setNum1(Double.valueOf(10.0));
        request.setNum2(Double.valueOf(0.0));

        when(calculator.calculate(Operation.DIVIDE, 10.0, 0.0))
                .thenThrow(new ArithmeticException("Division by zero"));

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Math error: Division by zero"));
    }

    @Test
    void calculate_UnsupportedOperation_ReturnsBadRequest() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.ADD);
        request.setNum1(Double.valueOf(5.0));
        request.setNum2(Double.valueOf(3.0));

        when(calculator.calculate(Operation.ADD, 5.0, 3.0))
                .thenThrow(new UnsupportedOperationException("Operation not supported"));

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unsupported operation: Operation not supported"));
    }

    @Test
    void calculate_InternalServerError_ReturnsServerError() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.ADD);
        request.setNum1(Double.valueOf(5.0));
        request.setNum2(Double.valueOf(3.0));

        when(calculator.calculate(Operation.ADD, 5.0, 3.0))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error: Unexpected error"));
    }

    // ========== Chain Calculation Tests ==========

    @Test
    void calculateChain_ValidRequest_ReturnsSuccessResponse() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(10.0));

        ChainOperationRequest op1 = new ChainOperationRequest();
        op1.setOperation(Operation.ADD);
        op1.setOperand(Double.valueOf(5.0));

        ChainOperationRequest op2 = new ChainOperationRequest();
        op2.setOperation(Operation.MULTIPLY);
        op2.setOperand(Double.valueOf(2.0));

        request.setOperations(Arrays.asList(op1, op2));

        when(calculator.startChain(10.0)).thenReturn(chainCalculator);
        when(chainCalculator.apply(Operation.ADD, 5.0)).thenReturn(chainCalculator);
        when(chainCalculator.apply(Operation.MULTIPLY, 2.0)).thenReturn(chainCalculator);
        when(chainCalculator.getResult()).thenReturn(30.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(30.0));

        verify(calculator).startChain(10.0);
        verify(chainCalculator).apply(Operation.ADD, 5.0);
        verify(chainCalculator).apply(Operation.MULTIPLY, 2.0);
        verify(chainCalculator).getResult();
    }

    @Test
    void calculateChain_SingleOperation_ReturnsCorrectResult() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(5.0));

        ChainOperationRequest op1 = new ChainOperationRequest();
        op1.setOperation(Operation.ADD);
        op1.setOperand(Double.valueOf(3.0));

        request.setOperations(Arrays.asList(op1));

        when(calculator.startChain(5.0)).thenReturn(chainCalculator);
        when(chainCalculator.apply(Operation.ADD, 3.0)).thenReturn(chainCalculator);
        when(chainCalculator.getResult()).thenReturn(8.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8.0));
    }

    @Test
    void calculateChain_NullInitialValue_ReturnsBadRequest() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(null);
        request.setOperations(Arrays.asList(new ChainOperationRequest()));

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Initial value cannot be null"));
    }

    @Test
    void calculateChain_EmptyOperations_ReturnsBadRequest() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(10.0));
        request.setOperations(Arrays.asList());

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Operations list cannot be null or empty"));
    }

    @Test
    void calculateChain_NullOperationInList_ReturnsBadRequest() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(10.0));

        ChainOperationRequest op1 = new ChainOperationRequest();
        op1.setOperation(null);
        op1.setOperand(Double.valueOf(5.0));

        request.setOperations(Arrays.asList(op1));

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Operation cannot be null"));
    }

    @Test
    void calculateChain_NullOperandInList_ReturnsBadRequest() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(10.0));

        ChainOperationRequest op1 = new ChainOperationRequest();
        op1.setOperation(Operation.ADD);
        op1.setOperand(null);

        request.setOperations(Arrays.asList(op1));

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input: Operand cannot be null"));
    }

    @Test
    void calculateChain_ArithmeticError_ReturnsBadRequest() throws Exception {
        // Given
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(Double.valueOf(10.0));

        ChainOperationRequest op1 = new ChainOperationRequest();
        op1.setOperation(Operation.DIVIDE);
        op1.setOperand(Double.valueOf(0.0));

        request.setOperations(Arrays.asList(op1));

        when(calculator.startChain(10.0)).thenReturn(chainCalculator);
        when(chainCalculator.apply(Operation.DIVIDE, 0.0))
                .thenThrow(new ArithmeticException("Division by zero"));

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Math error: Division by zero"));
    }

    // ========== GET Endpoints Tests ==========

    @Test
    void getSupportedOperations_ReturnsAllOperations() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/calculator/operations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Operation.values().length))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void health_ReturnsHealthStatus() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/calculator/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Calculator service is running"));
    }

    // ========== Integration-style Tests ==========

    @Test
    void calculate_ComplexMathOperation_ReturnsCorrectResult() throws Exception {
        // Given
        CalculationRequest request = new CalculationRequest();
        request.setOperation(Operation.MULTIPLY);
        request.setNum1(Double.valueOf(3.14159));
        request.setNum2(Double.valueOf(2.71828));

        double expectedResult = 3.14159 * 2.71828;
        when(calculator.calculate(Operation.MULTIPLY, 3.14159, 2.71828)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(post("/api/calculator/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    void calculateChain_ComplexChain_ReturnsCorrectResult() throws Exception {
        // Given: (10 + 5) * 2 - 3 / 1 = 27
        ChainCalculationRequest request = new ChainCalculationRequest();
        request.setInitialValue(10.0);

        ChainOperationRequest[] operations = {
                createChainOperation(Operation.ADD, 5.0),
                createChainOperation(Operation.MULTIPLY, 2.0),
                createChainOperation(Operation.SUBTRACT, 3.0),
                createChainOperation(Operation.DIVIDE, 1.0)
        };

        request.setOperations(Arrays.asList(operations));

        when(calculator.startChain(10.0)).thenReturn(chainCalculator);
        when(chainCalculator.apply(any(Operation.class), any(Double.class))).thenReturn(chainCalculator);
        when(chainCalculator.getResult()).thenReturn(27.0);

        // When & Then
        mockMvc.perform(post("/api/calculator/chain")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(27.0));
    }

    // ========== Helper Methods ==========

    private ChainOperationRequest createChainOperation(Operation operation, Double operand) {
        ChainOperationRequest chainOp = new ChainOperationRequest();
        chainOp.setOperation(operation);
        chainOp.setOperand(operand);
        return chainOp;
    }
}