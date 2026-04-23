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
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Service
@Slf4j
public class MigrationServiceImpl implements MigrationService {

    private final MigrationStrategyFactory strategyFactory;
    // Inject a Map of all instead of just one
    private final Map<String, AuthProvider> authProviders;
    private final Map<String, ConnectionValidator> connectionValidators;
    private final Map<String, BiServerValidator> biServerValidators;
    private final Map<String,FolderManager> folderManager;
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
        log.info("--- Starting Dynamic Workflow for Vendor: {} ---", request.getVendor());

        // 1. Auth Strategy
        AuthProvider authProvider = authProviders.get(request.getAuth().toLowerCase());
        if (authProvider == null) {
            throw new RuntimeException("Auth provider not found for: " + request.getDb());
        }
        authProvider.authenticate();

        // 2. DB Connection Strategy
        ConnectionValidator dbValidator = connectionValidators.get(request.getDb().toLowerCase());
        if (dbValidator == null) {
            throw new RuntimeException("DB Validator not found for: " + request.getDb());
        }
        dbValidator.validate();

        // 3. BI Server Validation Strategy
        BiServerValidator biServerValidator = biServerValidators.get(request.getBiValidator().toLowerCase());
        if (biServerValidator == null) {
            throw new RuntimeException("BI Validator not found for: " + request.getDb());
        }
        biServerValidator.validate();
        // 4. OS Utility Strategy
        FolderManager folderManager_ = folderManager.get(request.getOs().toLowerCase());
        if (folderManager_ == null) {
            throw new RuntimeException("Folder Support not found for: " + request.getDb());
        }
        folderManager_.createFolders();

        // 5. Extraction/Transformation Strategy (The Factory)
        Migration strategy = strategyFactory.getStrategy(request.getVendor().toLowerCase());
        if (strategy == null) {
            throw new RuntimeException("Strategy not found for: " + request.getDb());
        }
        strategy.getExtractor().extract();

        // 6. Multi-threaded Rationalization (Default or Vendor-Specific)
        Rationalizer rat = rationalizers.getOrDefault(
                request.getVendor().toLowerCase(),
                rationalizers.get("default")
        );
        rat.rationalise();

        strategy.getTransformer().transform();

        log.info("--- Workflow Successfully Initiated ---");
    }

}