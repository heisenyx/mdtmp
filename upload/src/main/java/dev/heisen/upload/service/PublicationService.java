package dev.heisen.upload.service;

import dev.heisen.upload.dto.PublicationUploadRequest;
import dev.heisen.upload.dto.PublicationUploadResponse;
import dev.heisen.upload.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private static final String MARKDOWN_CONTENT_TYPE = "text/markdown";

    private final S3Service s3Service;

    public PublicationUploadResponse create(PublicationUploadRequest request) {

        String generatedHash = RandomStringUtils.secureStrong().nextAlphanumeric(8);
        byte[] data = request.content().getBytes(StandardCharsets.UTF_8);

        try {
            s3Service.uploadFile(generatedHash, data, MARKDOWN_CONTENT_TYPE);
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to S3", e);
        }

        return PublicationUploadResponse.builder()
                .hash(generatedHash)
                .title(request.title())
                .author(request.author())
                .createdAt(Instant.now())
                .ttlMinutes(request.ttlMinutes())
                .build();
    }
}
