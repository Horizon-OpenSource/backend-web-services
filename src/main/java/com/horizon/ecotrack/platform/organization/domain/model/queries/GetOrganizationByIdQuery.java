package com.horizon.ecotrack.platform.organization.domain.model.queries;

/** Query to retrieve one organization by id. */
public record GetOrganizationByIdQuery(Long organizationId) {
    public GetOrganizationByIdQuery {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("A valid organization ID is required");
        }
    }
}
