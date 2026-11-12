package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

/** Input of a calculation: one activity record paired with the factor chosen for it. */
public record ActivityLine(String activityDataId, String sourceName, FootprintScope scope,
                           ActivityQuantity quantity, EmissionFactor factor) {
    public ActivityLine {
        activityDataId = CarbonRules.text(activityDataId, "activityDataId", 36);
        sourceName = CarbonRules.text(sourceName, "sourceName", 120);
        CarbonRules.required(scope, "scope");
        CarbonRules.required(quantity, "quantity");
        CarbonRules.required(factor, "emissionFactorId");
    }
}
