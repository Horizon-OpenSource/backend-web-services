package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Value object for an amount of greenhouse gas emissions expressed in kgCO2e. */
public record CarbonEmissions(BigDecimal kgCo2e) {
    public static final CarbonEmissions ZERO = new CarbonEmissions(BigDecimal.ZERO);

    public CarbonEmissions {
        if (kgCo2e == null || kgCo2e.signum() < 0) {
            throw new IllegalArgumentException("emissions must not be negative");
        }
        kgCo2e = kgCo2e.setScale(6, RoundingMode.HALF_UP);
    }

    public CarbonEmissions plus(CarbonEmissions other) {
        return new CarbonEmissions(kgCo2e.add(other.kgCo2e));
    }
}
