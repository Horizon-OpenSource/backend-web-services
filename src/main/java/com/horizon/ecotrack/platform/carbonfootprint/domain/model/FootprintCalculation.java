package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Aggregate root: the carbon footprint of one organization for one reporting period. */
@Entity
@Table(name = "footprint_calculations")
public class FootprintCalculation {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(name = "organization_id", nullable = false, length = 36, updatable = false)
    private String organizationId;
    @Column(nullable = false, length = 7, updatable = false)
    private String period;
    @Column(name = "calculated_at", nullable = false, updatable = false)
    private Instant calculatedAt;
    @OneToMany(mappedBy = "calculation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CalculationItem> items = new ArrayList<>();

    protected FootprintCalculation() {}

    private FootprintCalculation(String organizationId, ReportingPeriod period) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = CarbonRules.text(organizationId, "organizationId", 36);
        this.period = period.value();
        this.calculatedAt = Instant.now();
    }

    /**
     * Calculates the footprint of a period. Every activity record needs a factor of its own scope
     * and unit, and an activity record cannot be counted twice.
     */
    public static FootprintCalculation calculate(String organizationId, ReportingPeriod period, List<ActivityLine> lines) {
        CarbonRules.required(period, "period");
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("at least one activity record is required to calculate");
        }
        FootprintCalculation calculation = new FootprintCalculation(organizationId, period);
        Set<String> counted = new HashSet<>();
        for (ActivityLine line : lines) {
            if (!counted.add(line.activityDataId())) {
                throw new IllegalArgumentException("activity data " + line.activityDataId() + " is repeated");
            }
            if (line.factor().getScope() != line.scope()) {
                throw new IllegalArgumentException("factor '" + line.factor().getName() + "' belongs to "
                        + line.factor().getScope() + " but activity " + line.activityDataId() + " belongs to " + line.scope());
            }
            calculation.items.add(new CalculationItem(calculation, line));
        }
        return calculation;
    }

    public CarbonEmissions total() {
        return items.stream().map(CalculationItem::getEmissions).reduce(CarbonEmissions.ZERO, CarbonEmissions::plus);
    }

    public ScopeResult resultFor(FootprintScope scope) {
        List<CalculationItem> inScope = items.stream().filter(item -> item.getScope() == scope).toList();
        CarbonEmissions total = inScope.stream().map(CalculationItem::getEmissions)
                .reduce(CarbonEmissions.ZERO, CarbonEmissions::plus);
        return new ScopeResult(scope, total, inScope.size());
    }

    public List<ScopeResult> resultsByScope() {
        return List.of(resultFor(FootprintScope.SCOPE_1), resultFor(FootprintScope.SCOPE_2), resultFor(FootprintScope.SCOPE_3));
    }

    public String getId() { return id; }
    public String getOrganizationId() { return organizationId; }
    public ReportingPeriod getPeriod() { return ReportingPeriod.of(period); }
    public Instant getCalculatedAt() { return calculatedAt; }
    public List<CalculationItem> getItems() { return List.copyOf(items); }
}
