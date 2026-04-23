package com.aura.extractor;

import com.aura.core.event.ExtractionCompleteEvent;
import com.aura.model.MigrationRecord;
import com.aura.repository.MigrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("tableauExtractor")
@RequiredArgsConstructor
@Slf4j
public class TableauExtractor implements Extractor {

    private final MigrationRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void extract() {
        List<String> foundQueries = List.of(
                "SELECT * FROM tableau_users",
                "SELECT id FROM tableau_orders WHERE date > '2026-01-01'"
        );

        for (String query : foundQueries) {
            // Check the DB before saving a new record
            if (!repository.existsByExtractedSql(query)) {
                MigrationRecord record = new MigrationRecord();
                record.setVendor("tableau");
                record.setExtractedSql(query);
                record.setStatus("PENDING");

                MigrationRecord saved = repository.save(record);
                eventPublisher.publishEvent(new ExtractionCompleteEvent(saved.getId(), "tableau"));
            } else {
                log.info("Query already exists in DB, skipping: {}", query);
            }
        }
    }

}