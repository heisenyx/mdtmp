package dev.heisen.metadata.service;

import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.exception.MetadataNotFoundException;
import dev.heisen.upload.event.PublicationEvent;
import dev.heisen.metadata.mapper.PublicationMapper;
import dev.heisen.metadata.model.Publication;
import dev.heisen.metadata.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MetadataService {

    private final PublicationRepository repository;
    private final PublicationMapper mapper;

    @KafkaListener(topics = "publication-upload")
    public PublicationMetadataResponse create(PublicationEvent event) {

        Publication publication = Publication.builder()
                .hash(event.hash())
                .title(event.title())
                .author(event.author())
                .createdAt(event.createdAt())
                .ttlMinutes(event.ttlMinutes())
                .expired(false)
                .build();
        repository.save(publication);

        return mapper.toResponse(publication);
    }

    public PublicationMetadataResponse get(String hash) {

        Publication publication = repository.findByHashAndExpiredFalse(hash)
                .orElseThrow(() -> new MetadataNotFoundException("Publication metadata not found"));

        return mapper.toResponse(publication);
    }

    public void expire(String hash) {

        Publication publication = repository.findByHashAndExpiredFalse(hash)
                .orElseThrow(() -> new MetadataNotFoundException("Publication metadata not found"));
        publication.setExpired(true);
        repository.save(publication);
    }
}
