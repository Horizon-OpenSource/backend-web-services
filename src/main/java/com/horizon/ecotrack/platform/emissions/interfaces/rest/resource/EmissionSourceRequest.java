package com.horizon.ecotrack.platform.emissions.interfaces.rest.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmissionSourceRequest(
        @NotBlank @Size(max = 128) String facilityId,
        @NotBlank @Size(max = 120) String facilityName,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 100) String type,
        @NotBlank @Pattern(regexp = "SCOPE_[123]") String scope) {}
