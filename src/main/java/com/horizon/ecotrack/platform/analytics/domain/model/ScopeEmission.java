package com.horizon.ecotrack.platform.analytics.domain.model;

/**
 * Emissions of one scope in a period. A null amount means the scope has no result for that period.
 */
public record ScopeEmission(EmissionScope scope, Co2eTonnes tonnes) {
    public boolean hasData() {
        return tonnes != null;
    }
}
