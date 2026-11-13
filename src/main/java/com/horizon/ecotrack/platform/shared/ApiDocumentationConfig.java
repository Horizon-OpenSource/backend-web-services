package com.horizon.ecotrack.platform.shared;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "EcoTrack backend web services", version = "v1",
        description = "REST API of the EcoTrack platform"))
public class ApiDocumentationConfig {}