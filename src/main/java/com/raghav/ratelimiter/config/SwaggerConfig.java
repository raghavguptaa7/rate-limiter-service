package com.raghav.ratelimiter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("Token Bucket Rate Limiter API")

                        .version("1.0")

                        .description(
                                "Production-grade Thread Safe Token Bucket Rate Limiter built using Spring Boot."
                        )

                        .contact(
                                new Contact()
                                        .name("Raghav Gupta")
                                        .email("your-email@example.com")
                        )

                );
    }

}