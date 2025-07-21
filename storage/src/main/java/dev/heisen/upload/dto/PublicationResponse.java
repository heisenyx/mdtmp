package dev.heisen.upload.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PublicationResponse(
        String hash,
        String title,
        String author,
        Instant createdAt,
        int ttlMinutes
) {
}