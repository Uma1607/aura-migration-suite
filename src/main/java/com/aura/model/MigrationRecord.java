package com.aura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "migration_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MigrationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String extractedSql;

    private String vendor;
    private String status;
}