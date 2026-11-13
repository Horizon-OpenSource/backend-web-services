package com.horizon.ecotrack.platform.organization.application.commandservices;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.model.commands.CreateOrganizationCommand;
import com.horizon.ecotrack.platform.organization.domain.model.commands.UpdateOrganizationCommand;

/** Command operations for organizations. */
public interface OrganizationCommandService {
    Organization handle(CreateOrganizationCommand command);
    Organization handle(UpdateOrganizationCommand command);
}
