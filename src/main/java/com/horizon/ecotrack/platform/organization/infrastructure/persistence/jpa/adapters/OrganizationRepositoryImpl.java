package com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.adapters;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.repositories.OrganizationRepository;
import com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.assemblers.OrganizationPersistenceAssembler;
import com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.repositories.OrganizationPersistenceRepository;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final OrganizationPersistenceRepository persistenceRepository;

    public OrganizationRepositoryImpl(
            OrganizationPersistenceRepository persistenceRepository
    ) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Organization save(Organization organization) {
        var entity = OrganizationPersistenceAssembler
                .toPersistenceFromDomain(organization);

        var saved = persistenceRepository.save(entity);

        return OrganizationPersistenceAssembler
                .toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Organization> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(OrganizationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Organization> findAll() {
        return persistenceRepository.findAll()
                .stream()
                .map(OrganizationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
