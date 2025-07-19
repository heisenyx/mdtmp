package dev.heisen.metadata.controller;

import dev.heisen.metadata.dto.PublicationMetadataRequest;
import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping
    public ResponseEntity<PublicationMetadataResponse> getMetadata(
            @RequestBody PublicationMetadataRequest request
    ) {
        PublicationMetadataResponse response = metadataService.create(request);
        return ResponseEntity.ok(response);
    }
}
