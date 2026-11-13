package com.horizon.ecotrack.platform.organization.domain.model.aggregates;

import com.horizon.ecotrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Represents a site belonging to an organization.
 *
 * @author Javier Tello Murga
 */
@Getter
public class Site extends AbstractDomainAggregateRoot<Site> {

    private Long id;
    private Long organizationId;
    private String name;
    private String location;
    private boolean active;
    private int assignedUsers;

    public Site(
            Long organizationId,
            String name,
            String location,
            boolean active
    ) {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException(
                    "A valid organization ID is required"
            );
        }

        this.organizationId = organizationId;
        this.assignedUsers = 0;

        updateInformation(name, location, active);
    }

    public Site updateInformation(
            String name,
            String location,
            boolean active
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Site name is required"
            );
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException(
                    "Site location is required"
            );
        }

        this.name = name.trim();
        this.location = location.trim();
        this.active = active;

        return this;
    }

    public static Site reconstitute(
            Long id,
            Long organizationId,
            String name,
            String location,
            boolean active,
            int assignedUsers
    ) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "A valid site ID is required"
            );
        }

        if (assignedUsers < 0) {
            throw new IllegalArgumentException(
                    "Assigned users cannot be negative"
            );
        }

        Site site = new Site(
                organizationId,
                name,
                location,
                active
        );

        site.id = id;
        site.assignedUsers = assignedUsers;

        return site;
    }
}
