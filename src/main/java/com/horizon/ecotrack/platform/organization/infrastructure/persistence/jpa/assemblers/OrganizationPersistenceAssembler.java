package com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.assemblers;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.entities.OrganizationPersistenceEntity;

public final class OrganizationPersistenceAssembler {

    private OrganizationPersistenceAssembler() {}

    public static Organization toDomainFromPersistence(
            OrganizationPersistenceEntity entity
    ) {
        return Organization.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getIndustry(),
                entity.getAddressLine1(),
                entity.getAddressLine2(),
                entity.getCity(),
                entity.getStateProvince(),
                entity.getPostalCode(),
                entity.getCountry(),
                entity.getContactEmail(),
                entity.getPhone(),
                entity.getActiveUsers()
        );
    }

    public static OrganizationPersistenceEntity toPersistenceFromDomain(
            Organization organization
    ) {
        var entity = new OrganizationPersistenceEntity();

        entity.setId(organization.getId());
        entity.setName(organization.getName());
        entity.setIndustry(organization.getIndustry());
        entity.setAddressLine1(organization.getAddressLine1());
        entity.setAddressLine2(organization.getAddressLine2());
        entity.setCity(organization.getCity());
        entity.setStateProvince(organization.getStateProvince());
        entity.setPostalCode(organization.getPostalCode());
        entity.setCountry(organization.getCountry());
        entity.setContactEmail(organization.getContactEmail());
        entity.setPhone(organization.getPhone());
        entity.setActiveUsers(organization.getActiveUsers());

        return entity;
    }
}
