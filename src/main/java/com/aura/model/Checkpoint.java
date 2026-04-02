package com.aura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor // Lombok handles the empty constructor
public class Checkpoint {
    @Id
    private String stepName;
    private Long lastProcessedId;
    private LocalDateTime timestamp;

    // Custom constructor for your .orElse() logic
    public Checkpoint(String stepName, Long lastProcessedId, LocalDateTime timestamp) {
        this.stepName = stepName;
        this.lastProcessedId = lastProcessedId;
        this.timestamp = timestamp;
    }
}
