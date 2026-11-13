package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GenerateConsolidatedReportRequest(
        @Schema(example = "1") @NotNull @Positive Long organizationId) {
}
