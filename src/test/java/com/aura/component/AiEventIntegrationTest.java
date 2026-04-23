package com.aura.component;

import com.aura.api.dto.MigrationRequest;
import com.aura.core.service.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class AiEventIntegrationTest {

    @Autowired
    private MigrationService migrationService;

    @Test
    void testFullWorkflowWithAsyncAiAudit() throws InterruptedException {
        log.info("Starting Integration Test: Event-Driven AI Audit");

        // 1. Set up a dummy request
        MigrationRequest request = new MigrationRequest();
        request.setVendor("tableau");
        request.setAuth("sso");
        request.setDb("postgresvalidator");
        request.setBiValidator("tableauvalidator");
        request.setOs("win");
        request.setUploader("file");

        // 2. Execute the workflow
        // publish the event and move on immediately
        migrationService.executeFullWorkflow(request);

        log.info("Core workflow call finished. Waiting for Background AI and Rationalizer threads...");
        // 3. Keep the test alive for a few seconds
        Thread.sleep(20000);
        // before the test process exits.
        log.info("Test finished. Check logs for [AuraAI-1] activity.");
    }
}