package com.aura.repository;

import com.aura.model.MigrationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MigrationRepository extends JpaRepository<MigrationRecord, Long> {
    // Spring will automatically implement this if 'extractedSql'
    // is a field in your MigrationRecord class
    boolean existsByExtractedSql(String extractedSql);
    List<MigrationRecord> findByStatus(String status);
}