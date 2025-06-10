# Flexible-calculator

A production-quality, extensible calculator implementation in Java that demonstrates clean code practices, object-oriented design principles, and maintainability. This project showcases adherence to the Open-Closed Principle and supports both single operations and chained calculations.

## Features

### Core Functionality
- **Basic Operations**: Addition, subtraction, multiplication, division via enum-based operations
- **Single Calculations**: Perform individual operations between two numbers
- **Chained Operations**: Execute multiple operations sequentially on a running total
- **Extensible Design**: Add new operations without modifying existing Calculator code
- **Error Handling**: Graceful handling of unsupported operations and invalid inputs
- **IoC Compatible**: Designed for dependency injection and easy testing

## Architecture Overview

### Core Components

#### 1. Operation Enum
Defines supported mathematical operations:
```java
public enum Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
    // New operations can be added here
}
```

#### 2. Calculator Class
Main calculation engine with two key methods:
- `calculate(Operation op, Number num1, Number num2)` - Single operation
- Chaining methods for sequential operations

#### 3. Operation Strategy Pattern
Extensible operation handling without modifying core Calculator code.

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+ (for dependency management)
- Spring Boot 2.7+ (for REST API)
- JUnit 5 (for testing)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/gunjalgupta/Flexible-calculator.git
cd Flexible-calculator
```

2. Build the project:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Run tests:
```bash
mvn test
```

The application will start on `http://localhost:8080`

## REST API Usage

The calculator is also available as a REST API with the following endpoints:

### 1. Single Calculation
**POST** `/api/calculator/calculate`

Performs a single operation between two numbers.

**Request Body:**
```json
{
  "operation": "ADD",
  "num1": 5.0,
  "num2": 3.0
}
```

**Response:**
```json
{
  "result": 8.0,
  "success": true
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/calculator/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation": "ADD", "num1": 5.0, "num2": 3.0}'
```

### 2. Chained Calculations
**POST** `/api/calculator/chain`

Performs multiple operations sequentially starting with an initial value.

**Request Body:**
```json
{
  "initialValue": 10.0,
  "operations": [
    {"operation": "ADD", "operand": 5.0},
    {"operation": "MULTIPLY", "operand": 2.0}
  ]
}
```

**Response:**
```json
{
  "result": 30.0,
  "success": true
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/calculator/chain \
  -H "Content-Type: application/json" \
  -d '{
    "initialValue": 10.0,
    "operations": [
      {"operation": "ADD", "operand": 5.0},
      {"operation": "MULTIPLY", "operand": 2.0}
    ]
  }'
```

### 3. Get Supported Operations
**GET** `/api/calculator/operations`

Returns a list of all supported operations.

**Response:**
```json
["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"]
```

**cURL Example:**
```bash
curl http://localhost:8080/api/calculator/operations
```

### 4. Health Check
**GET** `/api/calculator/health`

Simple health check endpoint to verify the service is running.

**Response:**
```
Calculator service is running
```

**cURL Example:**
```bash
curl http://localhost:8080/api/calculator/health
```

## API Error Handling

### Error Response Format
When an error occurs, the API returns an error response:

```json
{
  "result": null,
  "success": false,
  "error": "Error message describing what went wrong"
}
```

### Common Error Scenarios

#### 1. Invalid Operation
```bash
curl -X POST http://localhost:8080/api/calculator/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation": "INVALID_OP", "num1": 5.0, "num2": 3.0}'
```
**Response (400 Bad Request):**
```json
{
  "error": "Unsupported operation: INVALID_OP is not supported"
}
```

#### 2. Division by Zero
```bash
curl -X POST http://localhost:8080/api/calculator/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation": "DIVIDE", "num1": 10.0, "num2": 0.0}'
```
**Response (400 Bad Request):**
```json
{
  "error": "Math error: Division by zero"
}
```

#### 3. Missing Required Fields
```bash
curl -X POST http://localhost:8080/api/calculator/calculate \
  -H "Content-Type: application/json" \
  -d '{"operation": "ADD", "num1": 5.0}'
```
**Response (400 Bad Request):**
```json
{
  "error": "Invalid input: Numbers cannot be null"
}
```

### Error Handling (Java API)
```java
try {
    calculator.calculate(Operation.DIVIDE, 10, 0); // Division by zero
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
}

try {
    calculator.calculate(Operation.UNSUPPORTED_OP, 5, 3); // Unsupported operation
} catch (UnsupportedOperationException e) {
    System.out.println("Error: " + e.getMessage());
}
```
