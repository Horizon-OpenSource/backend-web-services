package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import java.math.BigDecimal;

/** Value object for a measured activity amount together with its unit. */
public record ActivityQuantity(BigDecimal amount, String unit) {
    public ActivityQuantity {
        amount = CarbonRules.positive(amount, "quantity");
        unit = CarbonRules.text(unit, "unit", 20);
    }
}
