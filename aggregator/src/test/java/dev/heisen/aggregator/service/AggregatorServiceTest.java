package dev.heisen.aggregator.service;

import dev.heisen.aggregator.client.MetadataClient;
import dev.heisen.aggregator.client.StorageClient;
import dev.heisen.aggregator.dto.AggregatedPublication;
import dev.heisen.aggregator.dto.PublicationMetadataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AggregatorServiceTest {

    @Mock
    private StorageClient storageClient;

    @Mock
    private MetadataClient metadataClient;

    @InjectMocks
    private AggregatorService aggregatorService;

    @Test
    void testAggregate_shouldReturnsAggregatedPublication() {

        String hash = "test-hash";
        String content = "test-content";
        Instant timestamp = Instant.now();
        PublicationMetadataResponse metadata = PublicationMetadataResponse.builder()
                .hash("test-hash")
                .title("test-title")
                .author("test-author")
                .createdAt(timestamp)
                .ttlMinutes(5)
                .isExpired(false)
                .build();

        when(storageClient.getContent(hash)).thenReturn(content);
        when(metadataClient.getPublicationMetadata(hash)).thenReturn(metadata);

        AggregatedPublication result = aggregatorService.aggregate(hash);

        assertNotNull(result);
        assertEquals(hash, result.hash());
        assertEquals(content, result.content());
        assertEquals("test-title", result.title());
        assertEquals("test-author", result.author());
        assertEquals(timestamp, result.createdAt());
        assertEquals(5, result.ttlMinutes());
        assertFalse(result.isExpired());

        verify(storageClient).getContent(hash);
        verify(metadataClient).getPublicationMetadata(hash);
    }

    @Test
    void whenMetadataExpired_thenIsExpiredTrue() {

        String hash = "expired-hash";
        String content = "test-content";
        Instant timestamp = Instant.now();
        PublicationMetadataResponse metadata = PublicationMetadataResponse.builder()
                .hash("test-hash")
                .title("test-title")
                .author("test-author")
                .createdAt(timestamp)
                .ttlMinutes(5)
                .isExpired(true)
                .build();

        when(storageClient.getContent(hash)).thenReturn(content);
        when(metadataClient.getPublicationMetadata(hash)).thenReturn(metadata);

        AggregatedPublication result = aggregatorService.aggregate(hash);

        assertTrue(result.isExpired());
        assertEquals("test-title", result.title());
        assertEquals("test-content", result.content());
    }
}