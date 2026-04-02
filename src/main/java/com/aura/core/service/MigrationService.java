package com.aura.core.service;

import com.aura.api.dto.MigrationRequest;

public interface MigrationService {
    /**
     * Executes the full end-to-end migration workflow.
     * @param request The type of migration (e.g., "boToPowerBi")
     */
    void executeFullWorkflow(MigrationRequest request);
}
