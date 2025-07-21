package dev.heisen.expirer.client;

import dev.heisen.expirer.dto.PublicationMetadataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "metadata-service", url = "${services.metadata.url}")
public interface MetadataClient {
    @PutMapping("/metadata/{hash}/expire")
    PublicationMetadataResponse expire(
            @PathVariable("hash") String hash
    );
}