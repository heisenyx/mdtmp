package dev.heisen.aggregator.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PublicationMetadataResponse(
        String hash,
        String title,
        String author,
        Instant createdAt,
        int ttlMinutes,
        boolean isExpired
) {
}
