package com.horizon.ecotrack.platform.organization.domain.repositories;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for the Organization aggregate.
 *
 * @author Javier Tello Murga
 */
public interface OrganizationRepository {

    Organization save(Organization organization);

    Optional<Organization> findById(Long id);

    List<Organization> findAll();
}
