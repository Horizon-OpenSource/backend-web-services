package com.horizon.ecotrack.platform.organization.domain.model.commands;

/**
 * Command to delete an existing site.
 *
 * @author Javier Tello Murga
 */
public record DeleteSiteCommand(Long siteId) {

    public DeleteSiteCommand {
        if (siteId == null || siteId <= 0) {
            throw new IllegalArgumentException(
                    "A valid site ID is required"
            );
        }
    }
}
