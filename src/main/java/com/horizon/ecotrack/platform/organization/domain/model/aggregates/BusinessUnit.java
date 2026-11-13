package com.horizon.ecotrack.platform.organization.domain.model.aggregates;

import com.horizon.ecotrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Represents a business unit belonging to an organization and site.
 *
 * @author Javier Tello Murga
 */
@Getter
public class BusinessUnit extends AbstractDomainAggregateRoot<BusinessUnit> {

    private Long id;
    private Long organizationId;
    private Long siteId;

    private String name;
    private String code;
    private String responsible;
    private String description;

    private boolean active;
    private int emissionSourceCount;

    public BusinessUnit(
            Long organizationId,
            Long siteId,
            String name,
            String code,
            String responsible,
            String description,
            boolean active
    ) {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException(
                    "A valid organization ID is required"
            );
        }

        this.organizationId = organizationId;
        this.emissionSourceCount = 0;

        updateInformation(
                siteId,
                name,
                code,
                responsible,
                description,
                active
        );
    }

    public BusinessUnit updateInformation(
            Long siteId,
            String name,
            String code,
            String responsible,
            String description,
            boolean active
    ) {
        if (siteId == null || siteId <= 0) {
            throw new IllegalArgumentException(
                    "A valid site ID is required"
            );
        }

        validateRequired(name, "Business unit name");
        validateRequired(code, "Business unit code");
        validateRequired(responsible, "Responsible person");

        this.siteId = siteId;
        this.name = name.trim();
        this.code = code.trim();
        this.responsible = responsible.trim();
        this.description = description != null
                ? description.trim()
                : "";

        this.active = active;

        return this;
    }

    public static BusinessUnit reconstitute(
            Long id,
            Long organizationId,
            Long siteId,
            String name,
            String code,
            String responsible,
            String description,
            boolean active,
            int emissionSourceCount
    ) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "A valid business unit ID is required"
            );
        }

        if (emissionSourceCount < 0) {
            throw new IllegalArgumentException(
                    "Emission source count cannot be negative"
            );
        }

        BusinessUnit businessUnit = new BusinessUnit(
                organizationId,
                siteId,
                name,
                code,
                responsible,
                description,
                active
        );

        businessUnit.id = id;
        businessUnit.emissionSourceCount = emissionSourceCount;

        return businessUnit;
    }

    private static void validateRequired(
            String value,
            String fieldName
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required"
            );
        }
    }
}
