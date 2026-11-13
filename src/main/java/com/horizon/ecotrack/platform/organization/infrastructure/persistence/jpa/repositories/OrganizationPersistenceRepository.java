package com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.repositories;

import com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.entities.OrganizationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationPersistenceRepository
        extends JpaRepository<OrganizationPersistenceEntity, Long> {
}
