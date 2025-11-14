package org.startlight.awsome.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Awsome API", version = "v1.0.0", description = "Awsome API 문서"))
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customOpenAPI() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .pathsToExclude("/profile/**", "/explorer/**", "/api/**")
                .packagesToScan("org.startlight.awsome.controller")
                .build();
    }
}

