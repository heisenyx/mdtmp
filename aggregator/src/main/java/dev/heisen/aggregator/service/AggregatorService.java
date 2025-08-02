package dev.heisen.aggregator.service;

import dev.heisen.aggregator.client.MetadataClient;
import dev.heisen.aggregator.client.StorageClient;
import dev.heisen.aggregator.dto.AggregatedPublication;
import dev.heisen.aggregator.dto.PublicationMetadata;
import dev.heisen.aggregator.exception.AggregateException;
import dev.heisen.aggregator.exception.PublicationNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AggregatorService {

    private static final Logger log = LoggerFactory.getLogger(AggregatorService.class);

    private final StorageClient storageClient;
    private final MetadataClient metadataClient;

    public AggregatedPublication aggregate(String hash) {

        PublicationMetadata metadata;
        try {
            metadata = metadataClient.getPublicationMetadata(hash);
        } catch (FeignException.NotFound e) {
            log.debug("Metadata for hash '{}' not found", hash);
            throw new PublicationNotFoundException("Publication metadata not found");
        } catch (Exception e) {
            log.info("Error fetching metadata for '{}': {}", hash, e.getMessage(), e);
            throw new AggregateException("Error fetching metadata");
        }

        String content;
        try {
            content = storageClient.getContent(hash);
        } catch (FeignException.NotFound e) {
            log.debug("Content for hash '{}' not found", hash);
            throw new PublicationNotFoundException("Publication content not found");
        } catch (Exception e) {
            log.error("Error fetching content for '{}': {}", hash, e.getMessage(), e);
            throw new AggregateException("Error fetching content");
        }

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