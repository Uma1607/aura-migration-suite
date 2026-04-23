package com.aura.core.strategy;

import com.aura.auth.AuthProvider;
import com.aura.auth.SsoAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StrategyTest {

    @Autowired
    private MigrationStrategyFactory factory;

    @Test
    void testFactory() {
        log.info("--- STARTING STRATEGY TEST ---");

        Migration strategy = factory.getStrategy("pbi");

        log.info("Strategy Found: {}",strategy.getClass().getSimpleName());

        assertNotNull(strategy);
        assertEquals("PowerBIMigrationStrategy", strategy.getClass().getSimpleName());

        log.info("--- TEST PASSED SUCCESSFULLY ---");

    }
}