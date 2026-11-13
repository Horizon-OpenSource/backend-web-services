package com.horizon.ecotrack.platform.organization.domain.model.commands;

/**
 * Command to update an existing site.
 *
 * @author Javier Tello Murga
 */
public record UpdateSiteCommand(
        Long siteId,
        String name,
        String location,
        boolean active
) {

    public UpdateSiteCommand {

        if (siteId == null || siteId <= 0) {
            throw new IllegalArgumentException(
                    "A valid site ID is required"
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
