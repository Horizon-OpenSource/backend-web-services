package com.horizon.ecotrack.platform.analytics.infrastructure.seed;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Loads demonstration indicators for organization 1 (Andina Export S.A.C.) when the table is empty, so Analytics
 * does not depend on other bounded contexts. The periods match the activity data of the Mock API: January has
 * no Scope 3 result (partial data), February is complete and March has no results (empty state).
 */
@Component
class AnalyticsDemoDataLoader implements CommandLineRunner {
    private static final Long DEMO_ORGANIZATION_ID = 1L;

    private final EnvironmentalIndicatorRepository indicators;

    AnalyticsDemoDataLoader(EnvironmentalIndicatorRepository indicators) {
        this.indicators = indicators;
    }

    @Override
    public void run(String... args) {
        if (indicators.count() > 0) {
            return;
        }
        seed("2026-01", 2.50, 12.05, null);
        seed("2026-02", 3.10, 11.40, 1.80);
    }

    private void seed(String period, Double scope1, Double scope2, Double scope3) {
        add(period, EmissionScope.SCOPE_1, scope1);
        add(period, EmissionScope.SCOPE_2, scope2);
        add(period, EmissionScope.SCOPE_3, scope3);
    }

    private void add(String period, EmissionScope scope, Double tonnes) {
        if (tonnes != null) {
            indicators.save(new EnvironmentalIndicator(DEMO_ORGANIZATION_ID, scope, ReportingPeriod.of(period),
                    Co2eTonnes.of(tonnes)));
        }
    }
}
