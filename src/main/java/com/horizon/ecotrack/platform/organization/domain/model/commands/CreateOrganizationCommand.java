package com.horizon.ecotrack.platform.organization.domain.model.commands;

/**
 * Command to register a new organization in EcoTrack.
 *
 * @author Javier Tello Murga
 */
public record CreateOrganizationCommand(
        String name,
        String industry,
        String addressLine1,
        String addressLine2,
        String city,
        String stateProvince,
        String postalCode,
        String country,
        String contactEmail,
        String phone,
        Integer activeUsers
) {
    public CreateOrganizationCommand {

        validateRequired(name, "Organization name");
        validateRequired(industry, "Industry");
        validateRequired(addressLine1, "Address");
        validateRequired(city, "City");
        validateRequired(stateProvince, "State or province");
        validateRequired(country, "Country");
        validateRequired(contactEmail, "Contact email");
        validateRequired(phone, "Phone");

        if (!contactEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Invalid contact email"
            );
        }

        if (activeUsers == null || activeUsers < 0) {
            throw new IllegalArgumentException(
                    "Active users cannot be negative"
            );
        }
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
