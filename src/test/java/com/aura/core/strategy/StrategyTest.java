package com.aura.core.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StrategyTest {

    @Autowired
    private MigrationStrategyFactory factory;

    @Test
    void testFactory() {
        System.out.println("--- STARTING STRATEGY TEST ---");

        Migration strategy = factory.getStrategy("pbi");

        System.out.println("Strategy Found: " + strategy.getClass().getSimpleName());

        assertNotNull(strategy);
        assertEquals("PowerBIMigrationStrategy", strategy.getClass().getSimpleName());

        System.out.println("--- TEST PASSED SUCCESSFULLY ---");
    }
}