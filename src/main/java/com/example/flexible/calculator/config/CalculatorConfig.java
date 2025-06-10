package com.example.flexible.calculator.config;

import com.example.flexible.calculator.Calculator;
import com.example.flexible.calculator.factory.OperationStrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for Calculator beans.
 * Demonstrates IoC container integration.
 */
@Configuration
public class CalculatorConfig {

    @Bean
    public OperationStrategyFactory operationStrategyFactory() {
        return new OperationStrategyFactory();
    }

    @Bean
    public Calculator calculator(OperationStrategyFactory strategyFactory) {
        return new Calculator(strategyFactory);
    }
}