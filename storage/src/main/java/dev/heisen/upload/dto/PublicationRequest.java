package dev.heisen.upload.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record PublicationRequest(

        @NotBlank
        @Size(max = 255)
        String title,

        @Nullable
        @Size(max = 255)
        String author,

        @NotBlank
        @Size(max = 128000) // 128kb
        String content,

        @NotNull
        @Min(1)
        @Max(40320) // month in minutes
        int ttlMinutes
) {
}
