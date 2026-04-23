package com.aura.component.ai;

import com.aura.repository.MigrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MigrationRecoveryRunner implements CommandLineRunner {

    private final MigrationRepository repository;
    private final AiEventListener aiEventListener;

    @Override
    public void run(String... args) {
        log.info("RECOVERY: Checking for 'PENDING' migrations...");

        // Fetch everything stuck in PENDING from the DB to do startup ops
        repository.findByStatus("PENDING").forEach(record -> {
            log.info("RECOVERY: Resuming audit for ID: {}", record.getId());
            // Call the public method we created in the listener
            aiEventListener.processRecord(record);
        });

        log.info("RECOVERY: Finished processing existing records.");
    }
}
