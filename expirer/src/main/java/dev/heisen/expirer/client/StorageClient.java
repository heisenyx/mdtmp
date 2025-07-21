package dev.heisen.expirer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "storage-service", url = "${services.storage.url}")
public interface StorageClient {
    @DeleteMapping("/content/{hash}")
    String delete(@PathVariable("hash") String hash);
}