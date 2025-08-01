package dev.heisen.metadata.service;

import dev.heisen.metadata.dto.PublicationMetadataResponse;
import dev.heisen.metadata.exception.MetadataNotFoundException;
import dev.heisen.metadata.mapper.PublicationMapper;
import dev.heisen.metadata.model.Publication;
import dev.heisen.metadata.repository.PublicationRepository;
import dev.heisen.upload.event.PublicationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

    @Mock
    private PublicationRepository repository;

    @Mock
    private PublicationMapper mapper;

    @InjectMocks
    private MetadataService metadataService;

    private PublicationEvent publicationEvent;

    @BeforeEach
    void setUp() {
        publicationEvent = PublicationEvent.builder()
                .hash("h")
                .title("t")
                .author("a")
                .createdAt(Instant.now())
                .ttlMinutes(5)
                .build();
    }

    @Test
    void testCreate_shouldSavePublicationToRepository() {

        Publication expected = Publication.builder()
                .hash(publicationEvent.hash())
                .title(publicationEvent.title())
                .author(publicationEvent.author())
                .createdAt(publicationEvent.createdAt())
                .ttlMinutes(publicationEvent.ttlMinutes())
                .expired(false)
                .build();

        metadataService.create(publicationEvent);

        ArgumentCaptor<Publication> captor = ArgumentCaptor.forClass(Publication.class);
        verify(repository).save(captor.capture());

        Publication saved = captor.getValue();

        assertNotNull(saved);
        assertEquals(expected, saved);
    }

    @Test
    void testCreate_shouldReturnCorrectResponse() {

        PublicationMetadataResponse expectedResponse = new PublicationMetadataResponse(
                publicationEvent.hash(),
                publicationEvent.title(),
                publicationEvent.author(),
                publicationEvent.createdAt(),
                publicationEvent.ttlMinutes(),
                false
        );

        when(mapper.toResponse(any(Publication.class))).thenReturn(expectedResponse);

        PublicationMetadataResponse actualResponse = metadataService.create(publicationEvent);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGet_shouldReturnCorrectResponse() {

        Publication publication = Publication.builder()
                .hash(publicationEvent.hash())
                .title(publicationEvent.title())
                .author(publicationEvent.author())
                .createdAt(publicationEvent.createdAt())
                .ttlMinutes(publicationEvent.ttlMinutes())
                .expired(false)
                .build();

        PublicationMetadataResponse expectedResponse = PublicationMetadataResponse.builder()
                .hash(publicationEvent.hash())
                .title(publicationEvent.title())
                .author(publicationEvent.author())
                .createdAt(publicationEvent.createdAt())
                .ttlMinutes(publicationEvent.ttlMinutes())
                .isExpired(false)
                .build();

        when(repository.findByHashAndExpiredFalse(publicationEvent.hash()))
                .thenReturn(Optional.of(publication));

        when(mapper.toResponse(any(Publication.class)))
                .thenReturn(expectedResponse);

        PublicationMetadataResponse actualResponse = metadataService.get(publicationEvent.hash());

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGet_withInvalidHash_shouldReturnPublicationNotFoundException() {
        assertThrows(
                MetadataNotFoundException.class
                , () -> metadataService.get("invalidHash")
        );
    }

    @Test
    void testExpire_shouldExpirePublication() {

        Publication publication = Publication.builder()
                .hash(publicationEvent.hash())
                .title(publicationEvent.title())
                .author(publicationEvent.author())
                .createdAt(publicationEvent.createdAt())
                .ttlMinutes(publicationEvent.ttlMinutes())
                .expired(false)
                .build();


        when(repository.findByHashAndExpiredFalse(publicationEvent.hash()))
                .thenReturn(Optional.of(publication));

        metadataService.expire(publicationEvent.hash());
        assertTrue(publication.isExpired());

        verify(repository).save(publication);
    }
}