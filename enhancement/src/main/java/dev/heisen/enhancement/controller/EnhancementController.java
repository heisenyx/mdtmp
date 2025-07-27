package dev.heisen.enhancement.controller;

import dev.heisen.enhancement.dto.EnhancementRequest;
import dev.heisen.enhancement.service.EnhancementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publications/enhance")
@RequiredArgsConstructor
@Validated
public class EnhancementController {

    private final EnhancementService enhancementService;

    @PostMapping
    public ResponseEntity<String> enhance(
            @Valid @RequestBody EnhancementRequest request
    ) {
        String response = enhancementService.enhance(request);
        return ResponseEntity.ok(response);
    }
}
