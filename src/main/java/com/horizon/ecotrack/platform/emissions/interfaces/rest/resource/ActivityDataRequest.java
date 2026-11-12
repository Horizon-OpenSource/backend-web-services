package com.horizon.ecotrack.platform.emissions.interfaces.rest.resource;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ActivityDataRequest(
        @NotBlank @Size(max = 36) String emissionSourceId,
        @NotBlank @Pattern(regexp = "^(?!0000)\\d{4}-(0[1-9]|1[0-2])$") String period,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal quantity,
        @NotBlank @Size(max = 20) String unit,
        @NotBlank @Pattern(regexp = "MANUAL|CSV|API") String origin) {}
