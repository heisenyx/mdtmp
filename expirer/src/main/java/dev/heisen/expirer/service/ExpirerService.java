package dev.heisen.expirer.service;

import dev.heisen.expirer.client.MetadataClient;
import dev.heisen.expirer.client.StorageClient;
import dev.heisen.expirer.exception.ExpirerException;
import dev.heisen.upload.event.PublicationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExpirerService {

    private final String ZSET_KEY = "publications:expirations";
    private final RedisTemplate<String, String> redis;
    private final MetadataClient metadata;
    private final StorageClient storage;

    @KafkaListener(topics = "publication-upload")
    public void uploadEvent(PublicationEvent event) {
        String hash = event.hash();

        long expireAt = event.createdAt()
                .plus(event.ttlMinutes(), ChronoUnit.MINUTES)
                .getEpochSecond();

        redis.opsForZSet().add(ZSET_KEY, hash, expireAt);
    }

    @Scheduled(fixedDelayString = "${expirer.scan-interval-ms}")
    public void scanAndExpire() {
        long now = Instant.now().getEpochSecond();

        Set<String> keys = redis.opsForZSet()
                .rangeByScore(ZSET_KEY, 0, now);

        if (keys == null ||keys.isEmpty()) return;

        for (String key : keys) {
            try {
                metadata.expire(key);
                storage.delete(key);

                redis.opsForZSet().remove(ZSET_KEY, key);
            } catch (Exception e) {
                throw new ExpirerException("Failed to expire key: " + key, e);
            }
        }
    }
}
