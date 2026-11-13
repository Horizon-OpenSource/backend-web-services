package com.horizon.ecotrack.platform.organization.application.internal.queryservices;

import com.horizon.ecotrack.platform.organization.application.queryservices.OrganizationQueryService;
import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetAllOrganizationsQuery;
import com.horizon.ecotrack.platform.organization.domain.model.queries.GetOrganizationByIdQuery;
import com.horizon.ecotrack.platform.organization.domain.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/** Handles organization read operations. */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryServiceImpl implements OrganizationQueryService {
    private final OrganizationRepository repository;

    public OrganizationQueryServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Organization> handle(GetOrganizationByIdQuery query) {
        return repository.findById(query.organizationId());
    }

    @Override
    public List<Organization> handle(GetAllOrganizationsQuery query) {
        return repository.findAll();
    }
}
