package dev.heisen.upload.service;

import dev.heisen.upload.dto.PublicationRequest;
import dev.heisen.upload.dto.PublicationResponse;
import dev.heisen.upload.event.PublicationEvent;
import dev.heisen.upload.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private static final String MARKDOWN_CONTENT_TYPE = "text/markdown";

    private final S3Service s3Service;
    private final KafkaTemplate<String, PublicationEvent> kafkaTemplate;

    public PublicationResponse create(PublicationRequest request) {

        String generatedHash = RandomStringUtils.secureStrong().nextAlphanumeric(8);
        byte[] data = request.content().getBytes(StandardCharsets.UTF_8);
        Instant now = Instant.now();

        try {
            s3Service.uploadFile(generatedHash, data, MARKDOWN_CONTENT_TYPE);
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to S3", e);
        }

        PublicationEvent event = PublicationEvent.builder()
                .hash(generatedHash)
                .title(request.title())
                .author(request.author())
                .createdAt(now)
                .ttlMinutes(request.ttlMinutes())
                .build();
        kafkaTemplate.send("publication-upload", event);

        return PublicationResponse.builder()
                .hash(generatedHash)
                .title(event.title())
                .author(event.author())
                .createdAt(now)
                .ttlMinutes(event.ttlMinutes())
                .build();
    }

    public String getContent(String hash) {

        String content;
        try {
            byte[] data = s3Service.getFile(hash);
            content = new String(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new StorageException("Failed to retrieve file from S3", e);
        }

        return content;
    }

    public void delete(String hash) {
        try {
            s3Service.deleteFile(hash);
        } catch (Exception e) {
            throw new StorageException("Failed to delete file from S3", e);
        }
    }
}
