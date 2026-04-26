package com.aura.component.ai;

import com.aura.core.event.ExtractionCompleteEvent;
import com.aura.model.MigrationRecord;
import com.aura.repository.MigrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("aiExecutor")
@Slf4j
@RequiredArgsConstructor
public class AiEventListener {

    private final MigrationRepository repository; // Inject repo
    private final SqlAuditorService auditorService;

    @EventListener
    @Async
    public void handleExtraction(ExtractionCompleteEvent event) {
        repository.findById(event.migrationId())
                .ifPresent(this::processRecord);
    }

    // Both the Event and Recovery use it
    public void processRecord(MigrationRecord record) {
        try {
            auditorService.performAudit(record.getExtractedSql());
            record.setStatus("AUDITED");
            repository.save(record);
        } catch (Exception e) {
            log.error("Failed to process record {}: {}", record.getId(), e.getMessage());
        }
    }
}