package dev.heisen.enhancement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enhancementApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enhancement Service API")
                        .version("1.0")
                );
    }
}
