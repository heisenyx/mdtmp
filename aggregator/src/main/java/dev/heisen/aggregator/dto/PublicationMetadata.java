package dev.heisen.aggregator.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PublicationMetadata(
        String hash,
        String title,
        String author,
        Instant createdAt,
        int ttlMinutes,
        boolean isExpired
) {
}
