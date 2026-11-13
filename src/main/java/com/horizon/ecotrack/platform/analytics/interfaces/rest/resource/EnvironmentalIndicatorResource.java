package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculated emissions of one scope in one period")
public record EnvironmentalIndicatorResource(
        @Schema(example = "1") Long id,
        @Schema(description = "Indicator type, which is the emission scope", example = "SCOPE_1") String type,
        @Schema(description = "Tonnes of CO2 equivalent", example = "42.5") double value,
        @Schema(description = "Period as YYYY-MM", example = "2026-01") String period) {
}
