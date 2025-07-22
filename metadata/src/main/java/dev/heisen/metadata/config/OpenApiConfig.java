package dev.heisen.metadata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI metadataApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Metadata Service API")
                        .version("1.0")
                );
    }
}
