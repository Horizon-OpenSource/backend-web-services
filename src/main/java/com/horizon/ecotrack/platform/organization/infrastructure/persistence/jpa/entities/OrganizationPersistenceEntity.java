package com.horizon.ecotrack.platform.organization.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
public class OrganizationPersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String industry;

    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String stateProvince;

    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Integer activeUsers;
}
