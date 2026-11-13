package com.horizon.ecotrack.platform.organization.application.internal.commandservices;

import com.horizon.ecotrack.platform.organization.application.commandservices.OrganizationCommandService;
import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.domain.model.commands.CreateOrganizationCommand;
import com.horizon.ecotrack.platform.organization.domain.model.commands.UpdateOrganizationCommand;
import com.horizon.ecotrack.platform.organization.domain.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

/** Handles organization write operations. */
@Service
public class OrganizationCommandServiceImpl implements OrganizationCommandService {
    private final OrganizationRepository repository;

    public OrganizationCommandServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Organization handle(CreateOrganizationCommand command) {
        Organization organization = new Organization(
                command.name(), command.industry(), command.addressLine1(),
                command.addressLine2(), command.city(), command.stateProvince(),
                command.postalCode(), command.country(), command.contactEmail(),
                command.phone(), command.activeUsers());
        return repository.save(organization);
    }

    @Override
    @Transactional
    public Organization handle(UpdateOrganizationCommand command) {
        Organization organization = repository.findById(command.organizationId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Organization not found: " + command.organizationId()));

        organization.updateInformation(
                command.name(), command.industry(), command.addressLine1(),
                command.addressLine2(), command.city(), command.stateProvince(),
                command.postalCode(), command.country(), command.contactEmail(),
                command.phone());
        organization.updateActiveUsers(command.activeUsers());
        return repository.save(organization);
    }
}
