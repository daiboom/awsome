package org.startlight.awsome.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(info = @Info(title = "Awsome API", version = "v1.0.0", description = "Awsome API 문서"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customOpenAPI() {
        String[] paths = { "/users/**", "/admin/**" };

        return GroupedOpenApi.builder().group("Awsome API").pathsToMatch(paths)
                .build();
    }
}
