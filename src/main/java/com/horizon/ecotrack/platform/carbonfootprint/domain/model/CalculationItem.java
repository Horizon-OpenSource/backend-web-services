package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "calculation_items")
public class CalculationItem {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calculation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_calculation"))
    private FootprintCalculation calculation;
    @Column(name = "activity_data_id", nullable = false, length = 36)
    private String activityDataId;
    @Column(name = "source_name", nullable = false, length = 120)
    private String sourceName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FootprintScope scope;
    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;
    @Column(nullable = false, length = 20)
    private String unit;
    @Column(name = "emission_factor_id", nullable = false, length = 36)
    private String emissionFactorId;
    @Column(name = "factor_name", nullable = false, length = 120)
    private String factorName;
    @Column(name = "factor_value", nullable = false, precision = 18, scale = 6)
    private BigDecimal factorValue;
    @Column(nullable = false, precision = 24, scale = 6)
    private BigDecimal emissions;

    protected CalculationItem() {}

    CalculationItem(FootprintCalculation calculation, ActivityLine line) {
        this.id = UUID.randomUUID().toString();
        this.calculation = calculation;
        this.activityDataId = line.activityDataId();
        this.sourceName = line.sourceName();
        this.scope = line.scope();
        this.quantity = line.quantity().amount();
        this.unit = line.quantity().unit();
        this.emissionFactorId = line.factor().getId();
        this.factorName = line.factor().getName();
        this.factorValue = line.factor().getValue();
        this.emissions = line.factor().emissionsFor(line.quantity()).kgCo2e();
    }

    public String getId() { return id; }
    public String getActivityDataId() { return activityDataId; }
    public String getSourceName() { return sourceName; }
    public FootprintScope getScope() { return scope; }
    public BigDecimal getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getEmissionFactorId() { return emissionFactorId; }
    public String getFactorName() { return factorName; }
    public BigDecimal getFactorValue() { return factorValue; }
    public CarbonEmissions getEmissions() { return new CarbonEmissions(emissions); }
}