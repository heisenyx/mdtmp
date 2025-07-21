package dev.heisen.aggregator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "storage-service", url = "${services.storage.url}")
public interface StorageClient {
    @GetMapping("/content/{hash}")
    String getContent(@PathVariable("hash") String hash);
}
