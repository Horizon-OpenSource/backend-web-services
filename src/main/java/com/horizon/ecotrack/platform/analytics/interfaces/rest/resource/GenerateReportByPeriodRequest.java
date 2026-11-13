package com.horizon.ecotrack.platform.analytics.interfaces.rest.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record GenerateReportByPeriodRequest(
        @Schema(example = "1") @NotNull @Positive Long organizationId,
        @Schema(description = "Period as YYYY-MM", example = "2026-02")
        @NotBlank @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "must be a valid YYYY-MM") String period) {
}
