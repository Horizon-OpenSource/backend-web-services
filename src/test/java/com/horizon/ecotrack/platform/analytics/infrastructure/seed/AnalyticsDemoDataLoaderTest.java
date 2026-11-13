package com.horizon.ecotrack.platform.analytics.infrastructure.seed;

import static org.assertj.core.api.Assertions.assertThat;

import com.horizon.ecotrack.platform.analytics.application.InMemoryAnalyticsRepositories;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodSummary;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import org.junit.jupiter.api.Test;

class AnalyticsDemoDataLoaderTest {
    @Test
    void loadsTheDemoIndicatorsOnlyWhenThereAreNone() {
        var indicators = new InMemoryAnalyticsRepositories.Indicators();
        var loader = new AnalyticsDemoDataLoader(indicators);

        loader.run();
        loader.run();

        assertThat(indicators.count()).isEqualTo(5);
        var all = indicators.findByOrganizationId(1L);
        assertThat(PeriodSummary.from(ReportingPeriod.of("2026-01"), all).missingScopes()).hasSize(1);
        assertThat(PeriodSummary.from(ReportingPeriod.of("2026-01"), all).total().orElseThrow().toDouble())
                .isEqualTo(14.55);
        assertThat(PeriodSummary.from(ReportingPeriod.of("2026-02"), all).total().orElseThrow().toDouble())
                .isEqualTo(16.3);
        assertThat(PeriodSummary.from(ReportingPeriod.of("2026-03"), all).hasData()).isFalse();
    }
}
