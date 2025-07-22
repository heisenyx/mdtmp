package dev.heisen.upload.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI storageApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Storage Service API")
                        .version("1.0")
                );
    }
}
