package com.horizon.ecotrack.platform.organization.interfaces.rest.transform;

import com.horizon.ecotrack.platform.organization.domain.model.commands.CreateOrganizationCommand;
import com.horizon.ecotrack.platform.organization.interfaces.rest.resources.CreateOrganizationResource;

public final class CreateOrganizationCommandFromResourceAssembler {

    private CreateOrganizationCommandFromResourceAssembler() {}

    public static CreateOrganizationCommand toCommandFromResource(
            CreateOrganizationResource resource
    ) {
        return new CreateOrganizationCommand(
                resource.name(),
                resource.industry(),
                resource.addressLine1(),
                resource.addressLine2(),
                resource.city(),
                resource.stateProvince(),
                resource.postalCode(),
                resource.country(),
                resource.contactEmail(),
                resource.phone(),
                resource.activeUsers()
        );
    }
}
