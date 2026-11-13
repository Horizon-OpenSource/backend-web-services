package com.horizon.ecotrack.platform.organization.application.queryservices;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetOrganizationByIdQuery;
import java.util.List;
import java.util.Optional;

/** Query operations for organizations. */
public interface OrganizationQueryService {
    Optional<Organization> handle(GetOrganizationByIdQuery query);
    List<Organization> handle(GetAllOrganizationsQuery query);
}
