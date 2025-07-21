package dev.heisen.aggregator.client;

import dev.heisen.aggregator.dto.PublicationMetadataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "metadata-service", url = "${services.metadata.url}")
public interface MetadataClient {
    @GetMapping("/metadata/{hash}")
    PublicationMetadataResponse getPublicationMetadata(
            @PathVariable("hash") String hash
    );
}