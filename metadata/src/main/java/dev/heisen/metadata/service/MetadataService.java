package dev.heisen.metadata.service;

import dev.heisen.metadata.dto.PublicationMetadataRequest;
import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.mapper.PublicationMapper;
import dev.heisen.metadata.model.Publication;
import dev.heisen.metadata.repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MetadataService {

    private final PublicationRepository repository;
    private final PublicationMapper mapper;

    public PublicationMetadataResponse create(PublicationMetadataRequest request) {

        repository.findByHash(request.hash()).ifPresent(publication -> {
            throw new IllegalArgumentException("Publication already exists");
        });

        Publication publication = Publication.builder()
                .hash(request.hash())
                .title(request.title())
                .author(request.author())
                .createdAt(Instant.now())
                .ttlMinutes(request.ttlMinutes())
                .isExpired(false)
                .build();
        repository.save(publication);

        return mapper.toResponse(publication);
    }
}
