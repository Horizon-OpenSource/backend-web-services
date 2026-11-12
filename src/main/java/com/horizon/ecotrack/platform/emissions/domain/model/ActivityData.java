package com.horizon.ecotrack.platform.emissions.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Aggregate root for measured activity associated with an existing source. */
@Entity
@Table(name = "activity_data")
public class ActivityData {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emission_source_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity_emission_source"))
    private EmissionSource source;
    @Column(nullable = false, length = 7)
    private String period;
    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;
    @Column(nullable = false, length = 20)
    private String unit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ActivityOrigin origin;
    @Column(nullable = false, updatable = false)
    private Instant recordedAt;

    protected ActivityData() {}

    public ActivityData(EmissionSource source, String period, BigDecimal quantity, String unit, ActivityOrigin origin) {
        this.id = UUID.randomUUID().toString();
        this.recordedAt = Instant.now();
        changeDetails(source, period, quantity, unit, origin);
    }

    public void changeDetails(EmissionSource source, String period, BigDecimal quantity, String unit, ActivityOrigin origin) {
        EmissionSource validSource = EmissionRules.required(source, "emissionSourceId");
        String validPeriod = EmissionRules.period(period);
        BigDecimal validQuantity = EmissionRules.quantity(quantity);
        String validUnit = EmissionRules.text(unit, "unit", 20);
        ActivityOrigin validOrigin = EmissionRules.required(origin, "origin");
        this.source = validSource;
        this.period = validPeriod;
        this.quantity = validQuantity;
        this.unit = validUnit;
        this.origin = validOrigin;
    }

    public String getId() { return id; }
    public EmissionSource getSource() { return source; }
    public String getPeriod() { return period; }
    public BigDecimal getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public ActivityOrigin getOrigin() { return origin; }
    public Instant getRecordedAt() { return recordedAt; }
}
