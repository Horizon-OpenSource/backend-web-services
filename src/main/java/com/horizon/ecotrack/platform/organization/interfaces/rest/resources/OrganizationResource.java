package com.horizon.ecotrack.platform.organization.interfaces.rest.resources;

public record OrganizationResource(
        Long id,
        String name,
        String industry,
        String addressLine1,
        String addressLine2,
        String city,
        String stateProvince,
        String postalCode,
        String country,
        String contactEmail,
        String phone,
        Integer activeUsers
) {}
