package com.horizon.ecotrack.platform.organization.interfaces.rest.transform;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.model.commands.UpdateOrganizationCommand;
import com.horizon.ecotrack.platform.organization.interfaces.rest.resources.UpdateOrganizationResource;

public final class UpdateOrganizationCommandFromResourceAssembler {

    private UpdateOrganizationCommandFromResourceAssembler() {}

    public static UpdateOrganizationCommand toCommandFromResource(
            Long id,
            UpdateOrganizationResource resource,
            Organization current
    ) {
        return new UpdateOrganizationCommand(
                id,
                resource.name() != null
                        ? resource.name() : current.getName(),
                resource.industry() != null
                        ? resource.industry() : current.getIndustry(),
                resource.addressLine1() != null
                        ? resource.addressLine1() : current.getAddressLine1(),
                resource.addressLine2() != null
                        ? resource.addressLine2() : current.getAddressLine2(),
                resource.city() != null
                        ? resource.city() : current.getCity(),
                resource.stateProvince() != null
                        ? resource.stateProvince() : current.getStateProvince(),
                resource.postalCode() != null
                        ? resource.postalCode() : current.getPostalCode(),
                resource.country() != null
                        ? resource.country() : current.getCountry(),
                resource.contactEmail() != null
                        ? resource.contactEmail() : current.getContactEmail(),
                resource.phone() != null
                        ? resource.phone() : current.getPhone(),
                resource.activeUsers() != null
                        ? resource.activeUsers() : current.getActiveUsers()
        );
    }
}
