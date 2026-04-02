package com.aura.repository;

import com.aura.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // Spring now knows this repo manages 'Report' entities with 'Long' IDs
}
