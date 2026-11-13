package com.horizon.ecotrack.platform.organization.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public record UpdateOrganizationResource(
        String name,
        String industry,
        String addressLine1,
        String addressLine2,
        String city,
        String stateProvince,
        String postalCode,
        String country,
        @Email String contactEmail,
        String phone,
        @Min(0) Integer activeUsers
) {}
