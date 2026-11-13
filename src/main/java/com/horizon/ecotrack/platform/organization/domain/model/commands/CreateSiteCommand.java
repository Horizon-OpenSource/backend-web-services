package com.horizon.ecotrack.platform.organization.domain.model.commands;

/**
 * Command to create a new site for an organization.
 *
 * @param organizationId ID of the owning organization
 * @param name Name of the site
 * @param location Location of the site
 * @param active Whether the site is active
 *
 * @author Javier Tello Murga
 */
public record CreateSiteCommand(
        Long organizationId,
        String name,
        String location,
        boolean active
) {

    public CreateSiteCommand {

        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException(
                    "Organization ID must be greater than zero"
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Site name cannot be null or blank"
            );
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException(
                    "Site location cannot be null or blank"
            );
        }
    }
}
