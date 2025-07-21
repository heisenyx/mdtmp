package dev.heisen.metadata.service;

import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.upload.event.PublicationEvent;
import dev.heisen.metadata.mapper.PublicationMapper;
import dev.heisen.metadata.model.Publication;
import dev.heisen.metadata.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MetadataService {

    private final PublicationRepository repository;
    private final PublicationMapper mapper;

    @KafkaListener(topics = "publication-upload", groupId = "metadata-service")
    public PublicationMetadataResponse create(PublicationEvent event) {

        Publication publication = Publication.builder()
                .hash(event.hash())
                .title(event.title())
                .author(event.author())
                .createdAt(Instant.ofEpochMilli(event.createdAt()))
                .ttlMinutes(event.ttlMinutes())
                .isExpired(false)
                .build();
        repository.save(publication);

        return mapper.toResponse(publication);
    }

    public PublicationMetadataResponse get(String hash) {

        Publication publication = repository.findByHash(hash)
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        return mapper.toResponse(publication);
    }
}
