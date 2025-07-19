package dev.heisen.metadata.dto;

public record PublicationMetadataRequest(
        String hash,
        String title,
        String author,
        int ttlMinutes
) {
}
