package com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.assemblers;

import com.horizon.ecotrack.platform.organization.domain.model.aggregates.Organization;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrganizationPersistenceAssemblerTest {

    @Test
    void roundTripPreservesOrganizationData() {

        var original = Organization.reconstitute(
                7L,
                "Eco Empresa",
                "Agriculture",
                "Av. Central 1",
                "",
                "Lima",
                "Lima",
                "15001",
                "Peru",
                "contacto@eco.test",
                "999999999",
                2
        );

        var entity = OrganizationPersistenceAssembler
                .toPersistenceFromDomain(original);

        var restored = OrganizationPersistenceAssembler
                .toDomainFromPersistence(entity);

        assertEquals(7L, restored.getId());
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getContactEmail(), restored.getContactEmail());
        assertEquals(2, restored.getActiveUsers());
    }
}
