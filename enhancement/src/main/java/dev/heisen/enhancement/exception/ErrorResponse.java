package dev.heisen.enhancement.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String timestamp,
        int status,
        String message,
        String path
) {
}
