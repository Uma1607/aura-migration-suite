package com.aura.repository;

import com.aura.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // Spring manages 'Report' entities
}
