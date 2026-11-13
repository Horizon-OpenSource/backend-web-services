package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CalculationItemRequest(
        @NotBlank @Size(max = 36) String activityDataId,
        @NotBlank @Size(max = 120) String sourceName,
        @NotBlank @Pattern(regexp = "SCOPE_1|SCOPE_2|SCOPE_3") String scope,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal quantity,
        @NotBlank @Size(max = 20) String unit,
        @NotBlank @Size(max = 36) String emissionFactorId) {}