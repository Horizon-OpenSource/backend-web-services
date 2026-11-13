package com.horizon.ecotrack.platform.analytics.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * Comparison of the emissions of two periods. The variation goes from the first period to the second one.
 */
public record PeriodComparison(PeriodSummary from, PeriodSummary to) {
    public PeriodComparison {
        if (from == null || to == null || !from.hasData() || !to.hasData()) {
            throw new IllegalArgumentException("both periods need results to be compared");
        }
    }

    public BigDecimal variationTonnes() {
        return to.total().orElseThrow().value().subtract(from.total().orElseThrow().value());
    }

    /**
     * Variation as a percentage of the first period, or empty when the first period emitted nothing.
     */
    public Optional<BigDecimal> variationPercent() {
        BigDecimal base = from.total().orElseThrow().value();
        if (base.signum() == 0) {
            return Optional.empty();
        }
        return Optional.of(variationTonnes()
                .multiply(BigDecimal.valueOf(100))
                .divide(base, 2, RoundingMode.HALF_UP));
    }
}
