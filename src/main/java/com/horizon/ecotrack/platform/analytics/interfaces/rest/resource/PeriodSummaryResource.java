package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Emissions of an organization in one period, by scope")
public record PeriodSummaryResource(
        @Schema(example = "2026-02") String period,
        @Schema(description = "False when no scope has a result in the period") boolean hasData,
        @Schema(description = "Total tonnes of CO2 equivalent, null when there is no data", nullable = true)
        Double total,
        List<ScopeEmissionResource> scopes) {
}
