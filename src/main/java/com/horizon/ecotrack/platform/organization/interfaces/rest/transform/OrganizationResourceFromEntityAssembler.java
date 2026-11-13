package com.horizon.ecotrack.platform.organization.interfaces.rest.transform;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.interfaces.rest.resources.OrganizationResource;

public final class OrganizationResourceFromEntityAssembler {

    private OrganizationResourceFromEntityAssembler() {}

    public static OrganizationResource toResourceFromEntity(
            Organization organization
    ) {
        return new OrganizationResource(
                organization.getId(),
                organization.getName(),
                organization.getIndustry(),
                organization.getAddressLine1(),
                organization.getAddressLine2(),
                organization.getCity(),
                organization.getStateProvince(),
                organization.getPostalCode(),
                organization.getCountry(),
                organization.getContactEmail(),
                organization.getPhone(),
                organization.getActiveUsers()
        );
    }
}
