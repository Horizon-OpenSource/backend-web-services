package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Environmental report of an organization")
public record SustainabilityReportResource(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long organizationId,
        @Schema(description = "CONSOLIDATED or PERIOD", example = "PERIOD") String type,
        @Schema(description = "Period as YYYY-MM, null for a consolidated report", example = "2026-02",
                nullable = true) String period,
        @Schema(description = "Total tonnes of CO2 equivalent covered by the report", example = "83.5")
        double totalTonnes,
        @Schema(description = "GENERATED or DOWNLOADED", example = "GENERATED") String status,
        LocalDateTime generatedAt) {
}
