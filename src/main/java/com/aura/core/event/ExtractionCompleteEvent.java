package com.aura.core.event;
/**
 * The "Payload" of our event
 */
public record ExtractionCompleteEvent(Long migrationId, String vendor) {
}