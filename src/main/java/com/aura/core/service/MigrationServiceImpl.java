package com.aura.core.service;

import com.aura.api.dto.MigrationRequest;
import com.aura.auth.AuthProvider;
import com.aura.common.util.FolderManager;
import com.aura.core.strategy.Migration;
import com.aura.core.strategy.MigrationStrategyFactory;
import com.aura.rationalisor.Rationalizer;
import com.aura.validation.BiServerValidator;
import com.aura.validation.ConnectionValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MigrationServiceImpl implements MigrationService {

    private final MigrationStrategyFactory strategyFactory;
    private final Map<String, AuthProvider> authProviders;
    private final Map<String, ConnectionValidator> connectionValidators;
    private final Map<String, BiServerValidator> biServerValidators;
    private final Map<String,FolderManager> folderManager;
    // FIX: Inject a Map of all rationalizers instead of just one
    private final Map<String, Rationalizer> rationalizers;

    public MigrationServiceImpl(
            MigrationStrategyFactory strategyFactory,
            Map<String, AuthProvider> authProviders,
            Map<String, ConnectionValidator> connectionValidators,
            Map<String, BiServerValidator> biServerValidators,
            Map<String,FolderManager> folderManager,
            Map<String, Rationalizer> rationalizers) {
        this.strategyFactory = strategyFactory;
        this.authProviders = authProviders;
        this.connectionValidators = connectionValidators;
        this.biServerValidators = biServerValidators;
        this.folderManager = folderManager;
        this.rationalizers = rationalizers;
    }

    @Override
    public void executeFullWorkflow(MigrationRequest request) {
        System.out.println("--- Starting Dynamic Workflow for Vendor: " + request.getVendor() + " ---");

        // 1. Auth Strategy
        authProviders.get(request.getAuth().toLowerCase()).authenticate();

        // 2. DB Connection Strategy
        ConnectionValidator dbValidator = connectionValidators.get(request.getDb().toLowerCase());
        if (dbValidator == null) {
            throw new RuntimeException("DB Validator not found for: " + request.getDb());
        }
        dbValidator.validate();

        // 3. BI Server Validation Strategy
        BiServerValidator biServerValidator = biServerValidators.get(request.getBiValidator().toLowerCase());
        if (biServerValidator == null) {
            throw new RuntimeException("DB Validator not found for: " + request.getDb());
        }
        biServerValidator.validate();
        // 4. OS Utility Strategy
        folderManager.get(request.getOs().toLowerCase()).createFolders();

        // 5. Extraction/Transformation Strategy (The Factory)
        Migration strategy = strategyFactory.getStrategy(request.getVendor().toLowerCase());
        strategy.getExtractor().extract();

        // 6. Multi-threaded Rationalization (Default or Vendor-Specific)
        Rationalizer rat = rationalizers.getOrDefault(
                request.getVendor().toLowerCase(),
                rationalizers.get("default")
        );
        rat.rationalise();

        strategy.getTransformer().transform();

        System.out.println("--- Workflow Successfully Initiated ---");
    }

}