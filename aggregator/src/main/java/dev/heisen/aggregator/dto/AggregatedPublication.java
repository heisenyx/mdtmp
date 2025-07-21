package dev.heisen.aggregator.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AggregatedPublication(
        String hash,
        String title,
        String content,
        String author,
        Instant createdAt,
        int ttlMinutes,
        boolean isExpired
) {
}
