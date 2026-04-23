package com.aura.api;

import com.aura.api.dto.MigrationRequest;
import com.aura.core.service.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/migration")
@Slf4j
public class MigrationController {
    private final MigrationService migrationService;

    public MigrationController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runMigration(@RequestBody MigrationRequest request) {
        // Background execution with error logging to prevent "Silent Failures"
        CompletableFuture.runAsync(() -> {
            try {
                migrationService.executeFullWorkflow(request);
            } catch (Exception e) {
                log.error("CRITICAL ERROR IN WORKFLOW: {}", e.getMessage());
            }
        });

        return ResponseEntity.accepted().body("Migration for " + request.getVendor() + " is processing in background.");
    }
}
