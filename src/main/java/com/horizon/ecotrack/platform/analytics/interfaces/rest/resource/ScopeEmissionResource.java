package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Emissions of one scope; tonnes is null when the scope has no result in the period")
public record ScopeEmissionResource(
        @Schema(example = "SCOPE_3") String scope,
        @Schema(example = "15.0", nullable = true) Double tonnes) {
}
