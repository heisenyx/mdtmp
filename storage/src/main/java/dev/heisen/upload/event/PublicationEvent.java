package dev.heisen.upload.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PublicationEvent(
        String hash,
        String title,
        String author,
        Instant createdAt,
        int ttlMinutes
) {
}