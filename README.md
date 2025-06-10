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

## Design Decisions

### 1. Open-Closed Principle Implementation
- **Calculator Class**: Remains unchanged when new operations are added
- **Operation Enum**: Can be extended with new operations
- **Strategy Pattern**: New operation handlers implement common interface
- **Registration Mechanism**: Dynamic operation handler registration

### 2. IoC Compatibility
```java
// Constructor injection support
@Component
public class Calculator {
    private final Map<Operation, OperationHandler> operationHandlers;

    @Autowired
    public Calculator(Map<Operation, OperationHandler> operationHandlers) {
        this.operationHandlers = operationHandlers;
    }

    // Default constructor with standard operations
    public Calculator() {
        this(getDefaultOperationHandlers());
    }
}

// REST Controller with dependency injection
@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
    private final Calculator calculator;

    @Autowired
    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }
}
```

### 3. Error Handling Strategy
- **Input Validation**: Null checks and type validation
- **Operation Validation**: Graceful handling of unsupported operations
- **Mathematical Errors**: Division by zero, overflow handling
- **Custom Exceptions**: Meaningful error messages

### 4. Number Type Flexibility
- Supports various Number types (Integer, Double, BigDecimal)
- Maintains precision based on input types
- Handles type coercion appropriately

## Extensibility

### Adding New Operations

1. **Add to Operation Enum**:
```java
public enum Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, 
    POWER, SQRT, MOD  // New operations
}
```

2. **Implement Operation Handler**:
```java
public class PowerHandler implements OperationHandler {
    @Override
    public Number execute(Number num1, Number num2) {
        return Math.pow(num1.doubleValue(), num2.doubleValue());
    }
    
    @Override
    public Operation getSupportedOperation() {
        return Operation.POWER;
    }
}
```

3. **Register Handler**:
```java
operationHandlers.put(Operation.POWER, new PowerHandler());
```

## Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CalculatorTest

# Run API integration tests
mvn test -Dtest=CalculatorControllerTest

# Run tests with coverage
mvn test jacoco:report

# Run only unit tests (exclude integration tests)
mvn test -Dtest=!*ControllerTest
```

## Production Readiness

### Code Quality
- **Static Analysis**: Checkstyle, PMD, SpotBugs integration
- **Code Coverage**: Minimum 90% test coverage requirement
- **Documentation**: Comprehensive Javadoc documentation
- **Logging**: Structured logging with appropriate levels

### Build and Deployment
```bash
# Create production JAR
mvn clean package

# Run with specific profile
mvn clean package -Pproduction
```

