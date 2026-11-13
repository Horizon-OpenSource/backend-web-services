package com.horizon.ecotrack.platform.analytics.domain.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Emissions of an organization in one period, with one entry for each of the three scopes.
 */
public record PeriodSummary(ReportingPeriod period, List<ScopeEmission> scopes) {
    public PeriodSummary {
        scopes = List.copyOf(scopes);
    }

    public static PeriodSummary from(ReportingPeriod period, Collection<EnvironmentalIndicator> indicators) {
        var scopes = Arrays.stream(EmissionScope.values())
                .map(scope -> new ScopeEmission(scope, indicators.stream()
                        .filter(indicator -> indicator.getPeriod().equals(period) && indicator.getType() == scope)
                        .map(EnvironmentalIndicator::getValue)
                        .reduce(Co2eTonnes::plus)
                        .orElse(null)))
                .toList();
        return new PeriodSummary(period, scopes);
    }

    public boolean hasData() {
        return scopes.stream().anyMatch(ScopeEmission::hasData);
    }

    public Optional<Co2eTonnes> total() {
        return scopes.stream()
                .filter(ScopeEmission::hasData)
                .map(ScopeEmission::tonnes)
                .reduce(Co2eTonnes::plus);
    }

    public List<EmissionScope> missingScopes() {
        return scopes.stream().filter(scope -> !scope.hasData()).map(ScopeEmission::scope).toList();
    }
}
