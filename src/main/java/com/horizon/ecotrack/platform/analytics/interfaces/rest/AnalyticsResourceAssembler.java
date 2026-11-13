package com.horizon.ecotrack.platform.analytics.interfaces.rest;

import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodComparison;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodSummary;
import com.horizon.ecotrack.platform.analytics.domain.model.ScopeEmission;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.EnvironmentalIndicatorResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.PeriodComparisonResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.PeriodSummaryResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.ScopeEmissionResource;
import java.math.BigDecimal;

final class AnalyticsResourceAssembler {
    private AnalyticsResourceAssembler() {
    }

    static EnvironmentalIndicatorResource toResourceFrom(EnvironmentalIndicator indicator) {
        return new EnvironmentalIndicatorResource(indicator.getId(), indicator.getType().name(),
                indicator.getValue().toDouble(), indicator.getPeriod().value());
    }

    static PeriodSummaryResource toResourceFrom(PeriodSummary summary) {
        return new PeriodSummaryResource(summary.period().value(), summary.hasData(),
                summary.total().map(total -> total.toDouble()).orElse(null),
                summary.scopes().stream().map(AnalyticsResourceAssembler::toResourceFrom).toList());
    }

    static PeriodComparisonResource toResourceFrom(PeriodComparison comparison) {
        return new PeriodComparisonResource(toResourceFrom(comparison.from()), toResourceFrom(comparison.to()),
                comparison.variationTonnes().doubleValue(),
                comparison.variationPercent().map(BigDecimal::doubleValue).orElse(null));
    }

    private static ScopeEmissionResource toResourceFrom(ScopeEmission scope) {
        return new ScopeEmissionResource(scope.scope().name(), scope.hasData() ? scope.tonnes().toDouble() : null);
    }
}
