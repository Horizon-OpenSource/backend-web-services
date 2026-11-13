package com.horizon.ecotrack.platform.organization.domain.model.aggregates;

import com.horizon.ecotrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Aggregate root representing an organization in EcoTrack.
 *
 * @author Javier Tello Murga
 */
@Getter
public class Organization extends AbstractDomainAggregateRoot<Organization> {

    private Long id;
    private String name;
    private String industry;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String contactEmail;
    private String phone;
    private Integer activeUsers;

    public Organization(
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
        updateInformation(
                name,
                industry,
                addressLine1,
                addressLine2,
                city,
                stateProvince,
                postalCode,
                country,
                contactEmail,
                phone
        );

        updateActiveUsers(activeUsers);
    }

    public Organization updateInformation(
            String name,
            String industry,
            String addressLine1,
            String addressLine2,
            String city,
            String stateProvince,
            String postalCode,
            String country,
            String contactEmail,
            String phone
    ) {
        validateRequired(name, "Organization name");
        validateRequired(industry, "Industry");
        validateRequired(addressLine1, "Address");
        validateRequired(city, "City");
        validateRequired(stateProvince, "State or province");
        validateRequired(country, "Country");
        validateRequired(contactEmail, "Contact email");
        validateRequired(phone, "Phone");

        if (!contactEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Invalid contact email");
        }

        this.name = name.trim();
        this.industry = industry.trim();
        this.addressLine1 = addressLine1.trim();
        this.addressLine2 = addressLine2;
        this.city = city.trim();
        this.stateProvince = stateProvince.trim();
        this.postalCode = postalCode;
        this.country = country.trim();
        this.contactEmail = contactEmail.trim();
        this.phone = phone.trim();

        return this;
    }

    public Organization updateActiveUsers(Integer activeUsers) {
        if (activeUsers == null || activeUsers < 0) {
            throw new IllegalArgumentException(
                    "Active users cannot be negative"
            );
        }

        this.activeUsers = activeUsers;
        return this;
    }

    public static Organization reconstitute(
            Long id,
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
        var organization = new Organization(
                name,
                industry,
                addressLine1,
                addressLine2,
                city,
                stateProvince,
                postalCode,
                country,
                contactEmail,
                phone,
                activeUsers
        );

        organization.id = id;
        return organization;
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required"
            );
        }
    }
}
