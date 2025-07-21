package dev.heisen.upload.event;

import lombok.Builder;

@Builder
public record PublicationEvent(
        String hash,
        String title,
        String author,
        long createdAt,
        int ttlMinutes
) {
}