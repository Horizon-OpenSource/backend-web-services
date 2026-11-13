package com.horizon.ecotrack.platform.analytics.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodComparison;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodSummary;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportStatus;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportType;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalyticsDomainTest {
    private static final ReportingPeriod FEBRUARY = ReportingPeriod.of("2026-02");
    private static final ReportingPeriod MARCH = ReportingPeriod.of("2026-03");

    private static EnvironmentalIndicator indicator(ReportingPeriod period, EmissionScope scope, double tonnes) {
        return new EnvironmentalIndicator(1L, scope, period, Co2eTonnes.of(tonnes));
    }

    @Test
    void reportingPeriodAcceptsOnlyYearAndMonth() {
        assertThat(ReportingPeriod.of("2026-02").value()).isEqualTo("2026-02");
        assertThat(FEBRUARY).isLessThan(MARCH);
        for (String invalid : new String[] {"2026-13", "2026-00", "26-01", "2026/01", "", "0000-01"}) {
            assertThatThrownBy(() -> ReportingPeriod.of(invalid)).isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> ReportingPeriod.of(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void co2eTonnesKeepsTwoDecimalsAndRejectsNegativeAmounts() {
        assertThat(Co2eTonnes.of(1.005).toString()).isEqualTo("1.01");
        assertThat(Co2eTonnes.of(40).plus(Co2eTonnes.of(28.5)).toDouble()).isEqualTo(68.5);
        assertThatThrownBy(() -> Co2eTonnes.of(-0.01)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void summaryAddsTheScopesOfThePeriodAndFlagsMissingOnes() {
        var indicators = List.of(
                indicator(FEBRUARY, EmissionScope.SCOPE_1, 40.00),
                indicator(FEBRUARY, EmissionScope.SCOPE_1, 2.50),
                indicator(FEBRUARY, EmissionScope.SCOPE_2, 28.50),
                indicator(MARCH, EmissionScope.SCOPE_3, 99.00));

        var summary = PeriodSummary.from(FEBRUARY, indicators);

        assertThat(summary.hasData()).isTrue();
        assertThat(summary.total().orElseThrow().toDouble()).isEqualTo(71.0);
        assertThat(summary.scopes()).hasSize(3);
        assertThat(summary.scopes().get(0).tonnes().toDouble()).isEqualTo(42.5);
        assertThat(summary.missingScopes()).containsExactly(EmissionScope.SCOPE_3);
    }

    @Test
    void summaryOfAPeriodWithoutResultsHasNoData() {
        var summary = PeriodSummary.from(MARCH, List.of(indicator(FEBRUARY, EmissionScope.SCOPE_1, 10)));

        assertThat(summary.hasData()).isFalse();
        assertThat(summary.total()).isEmpty();
        assertThat(summary.missingScopes()).hasSize(3);
    }

    @Test
    void comparisonCalculatesTheVariationFromTheFirstPeriodToTheSecond() {
        var indicators = List.of(
                indicator(FEBRUARY, EmissionScope.SCOPE_1, 80.00),
                indicator(MARCH, EmissionScope.SCOPE_1, 84.00));

        var comparison = new PeriodComparison(PeriodSummary.from(FEBRUARY, indicators),
                PeriodSummary.from(MARCH, indicators));

        assertThat(comparison.variationTonnes()).isEqualByComparingTo("4.00");
        assertThat(comparison.variationPercent().orElseThrow()).isEqualByComparingTo("5.00");
    }

    @Test
    void comparisonRejectsAPeriodWithoutResults() {
        var indicators = List.of(indicator(FEBRUARY, EmissionScope.SCOPE_1, 80.00));

        assertThatThrownBy(() -> new PeriodComparison(PeriodSummary.from(FEBRUARY, indicators),
                PeriodSummary.from(MARCH, indicators))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void consolidatedReportHasNoPeriodAndStartsAsGenerated() {
        var report = SustainabilityReport.consolidated(1L, Co2eTonnes.of(245.6), LocalDateTime.now());

        assertThat(report.getType()).isEqualTo(ReportType.CONSOLIDATED);
        assertThat(report.getPeriod()).isNull();
        assertThat(report.getStatus()).isEqualTo(ReportStatus.GENERATED);
    }

    @Test
    void periodReportRequiresAPeriodAndCanBeMarkedAsDownloaded() {
        assertThatThrownBy(() -> SustainabilityReport.forPeriod(1L, null, Co2eTonnes.ZERO, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);

        var report = SustainabilityReport.forPeriod(1L, FEBRUARY, Co2eTonnes.of(83.5), LocalDateTime.now());
        report.markAsDownloaded();

        assertThat(report.getStatus()).isEqualTo(ReportStatus.DOWNLOADED);
        assertThat(report.getPeriod()).isEqualTo(FEBRUARY);
    }

    @Test
    void indicatorRequiresAPositiveOrganizationId() {
        assertThatThrownBy(() -> new EnvironmentalIndicator(0L, EmissionScope.SCOPE_1, FEBRUARY, Co2eTonnes.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
