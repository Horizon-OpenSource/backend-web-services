package com.horizon.ecotrack.platform.organization.interfaces.rest.resources;

import jakarta.validation.constraints.*;

public record CreateOrganizationResource(
        @NotBlank String name,
        @NotBlank String industry,
        @NotBlank String addressLine1,
        String addressLine2,
        @NotBlank String city,
        @NotBlank String stateProvince,
        String postalCode,
        @NotBlank String country,
        @NotBlank @Email String contactEmail,
        @NotBlank String phone,
        @NotNull @Min(0) Integer activeUsers
) {}
