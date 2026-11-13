package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportStatus;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportType;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import com.horizon.ecotrack.platform.analytics.domain.repository.SustainabilityReportRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaEnvironmentalIndicatorRepository.class, JpaSustainabilityReportRepository.class})
class AnalyticsPersistenceTest {
    @Autowired
    private EnvironmentalIndicatorRepository indicators;
    @Autowired
    private SustainabilityReportRepository reports;
    @Autowired
    private TestEntityManager entityManager;

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void indicatorsAreStoredWithTheirValueObjectsAndReadInPeriodAndScopeOrder() {
        indicators.save(new EnvironmentalIndicator(1L, EmissionScope.SCOPE_2, ReportingPeriod.of("2026-02"),
                Co2eTonnes.of(28.5)));
        indicators.save(new EnvironmentalIndicator(1L, EmissionScope.SCOPE_1, ReportingPeriod.of("2026-02"),
                Co2eTonnes.of(40)));
        indicators.save(new EnvironmentalIndicator(1L, EmissionScope.SCOPE_1, ReportingPeriod.of("2026-01"),
                Co2eTonnes.of(42.5)));
        indicators.save(new EnvironmentalIndicator(2L, EmissionScope.SCOPE_1, ReportingPeriod.of("2026-01"),
                Co2eTonnes.of(1)));
        flushAndClear();

        var found = indicators.findByOrganizationId(1L);

        assertThat(found).hasSize(3);
        assertThat(found).extracting(indicator -> indicator.getPeriod().value() + " " + indicator.getType())
                .containsExactly("2026-01 SCOPE_1", "2026-02 SCOPE_1", "2026-02 SCOPE_2");
        assertThat(found.get(0).getValue().toDouble()).isEqualTo(42.5);
        assertThat(found.get(0).getId()).isNotNull();
        assertThat(indicators.count()).isEqualTo(4);
    }

    @Test
    void reportsAreStoredWithAndWithoutPeriodAndKeepTheirStatus() {
        var consolidated = reports.save(SustainabilityReport.consolidated(1L, Co2eTonnes.of(245.6),
                LocalDateTime.of(2026, 11, 2, 9, 0)));
        var period = reports.save(SustainabilityReport.forPeriod(1L, ReportingPeriod.of("2026-02"),
                Co2eTonnes.of(83.5), LocalDateTime.of(2026, 11, 3, 9, 0)));
        period.markAsDownloaded();
        reports.save(period);
        flushAndClear();

        var history = reports.findByOrganizationId(1L);

        assertThat(history).extracting(SustainabilityReport::getId).containsExactly(period.getId(), consolidated.getId());
        assertThat(history.get(0).getStatus()).isEqualTo(ReportStatus.DOWNLOADED);
        assertThat(history.get(0).getPeriod()).isEqualTo(ReportingPeriod.of("2026-02"));
        assertThat(history.get(1).getType()).isEqualTo(ReportType.CONSOLIDATED);
        assertThat(history.get(1).getPeriod()).isNull();
        assertThat(history.get(1).getTotalTonnes().toDouble()).isEqualTo(245.6);
        assertThat(reports.findById(period.getId())).isPresent();
        assertThat(reports.findById(999L)).isEmpty();
    }
}
