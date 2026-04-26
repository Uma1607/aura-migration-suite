package com.aura.extractor;

import com.aura.core.event.ExtractionCompleteEvent;
import com.aura.model.MigrationRecord;
import com.aura.repository.MigrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("lookerExtractor")
@RequiredArgsConstructor
@Slf4j
public class LookerExtractor implements Extractor{

    private final MigrationRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void extract() {
        List<String> foundQueries = List.of(
                "SELECT * FROM looker_users",
                "SELECT id FROM looker_orders WHERE date > '2026-01-01'",
                "SELECT TOP 5 name, date FROM users (NOLOCK) WHERE date = '01/12/2024'"
        );

        for (String query : foundQueries) {
            // Check the DB before saving a new record
            if (!repository.existsByExtractedSql(query)) {
                MigrationRecord record = new MigrationRecord();
                record.setVendor("looker");
                record.setExtractedSql(query);
                record.setStatus("PENDING");

                MigrationRecord saved = repository.save(record);
                eventPublisher.publishEvent(new ExtractionCompleteEvent(saved.getId(), "looker"));
            } else {
                log.info("Query already exists in DB, skipping: {}", query);
            }
        }
    }

}
