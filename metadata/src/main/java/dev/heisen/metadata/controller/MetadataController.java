package dev.heisen.metadata.controller;

import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publication/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/{hash}")
    public ResponseEntity<PublicationMetadataResponse> getMetadata(
            @PathVariable("hash") String hash
    ) {
        PublicationMetadataResponse response = metadataService.get(hash);
        return ResponseEntity.ok(response);
    }
}
