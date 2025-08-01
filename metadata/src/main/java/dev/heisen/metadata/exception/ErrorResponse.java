package dev.heisen.metadata.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String timestamp,
        int status,
        String message,
        String path
) {
}
