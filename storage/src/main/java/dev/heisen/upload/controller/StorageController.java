package dev.heisen.upload.controller;

import dev.heisen.upload.dto.PublicationRequest;
import dev.heisen.upload.dto.PublicationResponse;
import dev.heisen.upload.service.PublicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publications")
@RequiredArgsConstructor
public class StorageController {

    private final PublicationService publicationService;

    @PostMapping("/publish")
    public ResponseEntity<PublicationResponse> publish(
            @Valid @RequestBody PublicationRequest request
    ) {
        PublicationResponse response = publicationService.create(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/content/{hash}")
    public ResponseEntity<String> getContent(
            @PathVariable("hash") String hash
    ) {
        String content = publicationService.getContent(hash);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/content/{hash}")
    public ResponseEntity<?> deleteContent(
            @PathVariable("hash") String hash
    ) {
        publicationService.delete(hash);
        return ResponseEntity.noContent().build();
    }
}
