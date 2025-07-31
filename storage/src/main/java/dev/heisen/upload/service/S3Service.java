package dev.heisen.upload.service;

import dev.heisen.upload.exception.ContentNotFoundException;
import dev.heisen.upload.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public void uploadFile(String key, byte[] data, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));
        } catch (Exception e) {
            log.error("Error while uploading file to S3: {}", e.getMessage(), e);
            throw new StorageException("Failed to upload file to S3", e);
        }
    }

    public byte[] getFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            return s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (NoSuchKeyException e) {
            throw new ContentNotFoundException("Publication content not found");
        } catch (Exception e) {
            log.error("Error while reading file from S3: {}", e.getMessage(), e);
            throw new StorageException("Failed to retrieve file from S3", e);
        }
    }

    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Error while deleting file from S3: {}", e.getMessage(), e);
            throw new StorageException("Failed to delete file from S3", e);
        }
    }
}
