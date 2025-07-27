package dev.heisen.enhancement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EnhancementRequest(

        @NotBlank
        @Size(min = 1, max = 255)
        String title,

        @NotBlank
        @Size(min = 1, max = 128000) // 128 Kb
        String content
) {
}
