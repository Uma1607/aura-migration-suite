package com.aura.repository;

import com.aura.model.Checkpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, String> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO checkpoint (step_name, last_processed_id, timestamp)
        VALUES (:stepName, :lastId, :ts)
        ON CONFLICT (step_name)\s
        DO UPDATE SET last_processed_id = EXCLUDED.last_processed_id,\s
                      timestamp = EXCLUDED.timestamp
       \s""", nativeQuery = true)
    void upsertCheckpoint(String stepName, Long lastId, LocalDateTime ts);
}