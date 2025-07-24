package dev.heisen.aggregator.service;

import dev.heisen.aggregator.client.MetadataClient;
import dev.heisen.aggregator.client.StorageClient;
import dev.heisen.aggregator.dto.AggregatedPublication;
import dev.heisen.aggregator.dto.PublicationMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregatorService {

    private final StorageClient storageClient;
    private final MetadataClient metadataClient;

    public AggregatedPublication aggregate(String hash) {

        String content = storageClient.getContent(hash);

        PublicationMetadata metadata = metadataClient.getPublicationMetadata(hash);

        return AggregatedPublication.builder()
                .hash(hash)
                .title(metadata.title())
                .content(content)
                .author(metadata.author())
                .createdAt(metadata.createdAt())
                .ttlMinutes(metadata.ttlMinutes())
                .isExpired(metadata.isExpired())
                .build();
    }
}