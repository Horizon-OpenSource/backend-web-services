package com.horizon.ecotrack.platform.shared;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/** OpenAPI metadata. Swagger UI: /swagger-ui/index.html, OpenAPI document: /v3/api-docs. */
@Configuration
@OpenAPIDefinition(info = @Info(title = "EcoTrack backend web services", version = "v1",
        description = "REST API of the EcoTrack platform"))
public class ApiDocumentationConfig {}
