package com.aura.core.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class MigrationStrategyFactory {
    private final Map<String, Migration> strategies;

    public MigrationStrategyFactory(Map<String, Migration> strategies) {
        this.strategies = strategies;
    }

    public Migration getStrategy(String type) {
        // This is now purely dynamic based on the @Component name
        Migration strategy = strategies.get(type.toLowerCase());
        if (strategy == null) {
            throw new RuntimeException("Strategy not found: " + type);
        }
        return strategy;
    }
}
