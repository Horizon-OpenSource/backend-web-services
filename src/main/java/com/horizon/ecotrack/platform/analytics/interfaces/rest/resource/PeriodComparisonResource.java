package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Comparison of two periods; the variation goes from the first period to the second one")
public record PeriodComparisonResource(
        PeriodSummaryResource from,
        PeriodSummaryResource to,
        @Schema(description = "Variation in tonnes of CO2 equivalent", example = "2.8") double variationTonnes,
        @Schema(description = "Variation as a percentage of the first period, null when the first period is zero",
                example = "3.1", nullable = true) Double variationPercent) {
}
