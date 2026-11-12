package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CalculateFootprintRequest(
        @NotBlank @Size(max = 36) String organizationId,
        @NotBlank @Pattern(regexp = "^(?!0000)\\d{4}-(0[1-9]|1[0-2])$") String period,
        @NotEmpty List<@Valid CalculationItemRequest> items) {}
