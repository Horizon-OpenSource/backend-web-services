package com.horizon.ecotrack.platform.analytics.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Calculated emissions of one organization for one scope in one period. The scope is the indicator type.
 */
@Entity
@Table(name = "environmental_indicators")
public class EnvironmentalIndicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private EmissionScope type;

    @Column(name = "period", nullable = false, length = 20)
    private ReportingPeriod period;

    @Column(name = "indicator_value", nullable = false, precision = 14, scale = 2)
    private Co2eTonnes value;

    protected EnvironmentalIndicator() {
    }

    public EnvironmentalIndicator(Long organizationId, EmissionScope type, ReportingPeriod period, Co2eTonnes value) {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId must be a positive number");
        }
        if (type == null || period == null || value == null) {
            throw new IllegalArgumentException("type, period and value are required");
        }
        this.organizationId = organizationId;
        this.type = type;
        this.period = period;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public EmissionScope getType() {
        return type;
    }

    public ReportingPeriod getPeriod() {
        return period;
    }

    public Co2eTonnes getValue() {
        return value;
    }
}
