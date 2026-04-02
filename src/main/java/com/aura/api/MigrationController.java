package com.aura.api;

import com.aura.api.dto.MigrationRequest;
import com.aura.core.service.MigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/migration")
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
                System.err.println("CRITICAL ERROR IN WORKFLOW: " + e.getMessage());
                e.printStackTrace();
            }
        });

        return ResponseEntity.accepted().body("Migration for " + request.getVendor() + " is processing in background.");
    }
}
