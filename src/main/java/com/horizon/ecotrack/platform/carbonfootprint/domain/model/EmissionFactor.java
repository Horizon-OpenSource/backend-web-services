package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/** Aggregate root: a factor that converts an activity amount into kgCO2e. */
@Entity
@Table(name = "emission_factors")
public class EmissionFactor {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(nullable = false, length = 120)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FootprintScope scope;
    @Column(nullable = false, length = 20)
    private String unit;
    @Column(name = "factor_value", nullable = false, precision = 18, scale = 6)
    private BigDecimal value;
    @Column(nullable = false, length = 160)
    private String source;

    protected EmissionFactor() {}

    public EmissionFactor(String name, FootprintScope scope, String unit, BigDecimal value, String source) {
        this.id = UUID.randomUUID().toString();
        changeDetails(name, scope, unit, value, source);
    }

    public void changeDetails(String name, FootprintScope scope, String unit, BigDecimal value, String source) {
        String validName = CarbonRules.text(name, "name", 120);
        FootprintScope validScope = CarbonRules.required(scope, "scope");
        String validUnit = CarbonRules.text(unit, "unit", 20);
        BigDecimal validValue = CarbonRules.positive(value, "factor value");
        String validSource = CarbonRules.text(source, "source", 160);
        this.name = validName;
        this.scope = validScope;
        this.unit = validUnit;
        this.value = validValue;
        this.source = validSource;
    }

    /** Converts an activity amount into emissions; the amount must use the factor's unit. */
    public CarbonEmissions emissionsFor(ActivityQuantity quantity) {
        CarbonRules.required(quantity, "quantity");
        if (!unit.equalsIgnoreCase(quantity.unit())) {
            throw new IllegalArgumentException("factor '" + name + "' uses unit " + unit
                    + " but the activity uses " + quantity.unit());
        }
        return new CarbonEmissions(quantity.amount().multiply(value));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public FootprintScope getScope() { return scope; }
    public String getUnit() { return unit; }
    public BigDecimal getValue() { return value; }
    public String getSource() { return source; }
}
