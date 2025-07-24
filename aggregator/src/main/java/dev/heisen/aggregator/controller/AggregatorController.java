package dev.heisen.aggregator.controller;

import dev.heisen.aggregator.dto.AggregatedPublication;
import dev.heisen.aggregator.service.AggregatorService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publications")
@RequiredArgsConstructor
@Validated
public class AggregatorController {

    private final AggregatorService aggregatorService;

    @GetMapping("/{hash}")
    public ResponseEntity<AggregatedPublication> getPublication(
            @Size(max = 8) @PathVariable String hash
    ) {
        AggregatedPublication publication = aggregatorService.aggregate(hash);
        return ResponseEntity.ok(publication);
    }
}
