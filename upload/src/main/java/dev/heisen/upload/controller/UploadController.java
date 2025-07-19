package dev.heisen.upload.controller;

import dev.heisen.upload.dto.PublicationUploadRequest;
import dev.heisen.upload.dto.PublicationUploadResponse;
import dev.heisen.upload.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UploadController {

    private final PublicationService publicationService;

    @PostMapping("/publish")
    public ResponseEntity<PublicationUploadResponse> publish(
            @RequestBody PublicationUploadRequest request
    ) {
        PublicationUploadResponse response = publicationService.create(request);

        return ResponseEntity.ok(response);
    }
}
