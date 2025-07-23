package dev.heisen.upload.service;

import dev.heisen.upload.dto.PublicationRequest;
import dev.heisen.upload.dto.PublicationResponse;
import dev.heisen.upload.event.PublicationEvent;
import dev.heisen.upload.exception.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private KafkaTemplate<String, PublicationEvent> kafkaTemplate;

    @InjectMocks
    private PublicationService publicationService;

    private PublicationRequest request;
    private final byte[] contentBytes = "hello‑world".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    void setUp() {
        request = PublicationRequest.builder()
                .title("t")
                .author("a")
                .content("hello‑world")
                .ttlMinutes(5)
                .build();
    }

    @Test
    void testCreate_shouldGenerateAn8CharAlphanumericHash() {
        PublicationResponse response = publicationService.create(request);

        ArgumentCaptor<String> hashCap = ArgumentCaptor.forClass(String.class);
        verify(s3Service).uploadFile(
                hashCap.capture(),
                any(),
                any()
        );
        String generatedHash = hashCap.getValue();

        assertNotNull(generatedHash);
        assertEquals(8, generatedHash.length());
        assertTrue(generatedHash.matches("[A-Za-z0-9]+"));

        assertEquals(generatedHash, response.hash());

        ArgumentCaptor<PublicationEvent> eventCap = ArgumentCaptor.forClass(PublicationEvent.class);
        verify(kafkaTemplate).send(eq("publication-upload"), eventCap.capture());
        assertEquals(generatedHash, eventCap.getValue().hash());
    }

    @Test
    void testCreate_shouldUploadToS3_andPublishEvent_andReturnResponse() {
        PublicationResponse response = publicationService.create(request);

        ArgumentCaptor<String> hashCap = ArgumentCaptor.forClass(String.class);
        verify(s3Service).uploadFile(
                hashCap.capture(),
                eq(contentBytes),
                eq("text/markdown")
        );
        String generatedHash = hashCap.getValue();

        assertEquals(generatedHash, response.hash());

        ArgumentCaptor<PublicationEvent> eventCap = ArgumentCaptor.forClass(PublicationEvent.class);
        verify(kafkaTemplate).send(eq("publication-upload"), eventCap.capture());
        assertEquals(generatedHash, eventCap.getValue().hash());

        assertEquals("t", response.title());
        assertEquals("a", response.author());
        assertEquals(5, response.ttlMinutes());
    }

    @Test
    void testGetContent_shouldReturnStringFromS3() {
        when(s3Service.getFile("myhash")).thenReturn(contentBytes);

        String content = publicationService.getContent("myhash");

        assertEquals("hello‑world", content);
        verify(s3Service).getFile("myhash");
    }

    @Test
    void testGetContent_whenS3Fails_shouldThrowStorageException() {
        when(s3Service.getFile("x")).thenThrow(new RuntimeException("nop"));

        StorageException ex = assertThrows(
                StorageException.class,
                () -> publicationService.getContent("x")
        );
        assertTrue(ex.getMessage().contains("Failed to retrieve file from S3"));
    }

    @Test
    void testDelete_shouldCallS3Delete() {
        // when
        publicationService.delete("h");

        // then
        verify(s3Service).deleteFile("h");
    }

    @Test
    void testDelete_whenS3Fails_shouldThrowStorageException() {
        doThrow(new RuntimeException("error"))
                .when(s3Service).deleteFile("z");

        StorageException ex = assertThrows(
                StorageException.class,
                () -> publicationService.delete("z")
        );
        assertTrue(ex.getMessage().contains("Failed to delete file from S3"));
    }
}