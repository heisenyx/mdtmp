package dev.heisen.aggregator.client;

import dev.heisen.aggregator.dto.PublicationMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "metadata-service", url = "${services.metadata.url}")
public interface MetadataClient {
    @GetMapping("/metadata/{hash}")
    PublicationMetadata getPublicationMetadata(
            @PathVariable("hash") String hash
    );
}