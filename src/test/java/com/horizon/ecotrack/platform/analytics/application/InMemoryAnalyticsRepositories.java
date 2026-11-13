package com.horizon.ecotrack.platform.analytics.application;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import com.horizon.ecotrack.platform.analytics.domain.repository.SustainabilityReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Repositories that keep everything in memory so the services and controllers can be tested without a database.
 */
public final class InMemoryAnalyticsRepositories {
    private InMemoryAnalyticsRepositories() {
    }

    public static final class Indicators implements EnvironmentalIndicatorRepository {
        private final List<EnvironmentalIndicator> data = new ArrayList<>();
        private final AtomicLong ids = new AtomicLong();

        @Override
        public EnvironmentalIndicator save(EnvironmentalIndicator indicator) {
            ReflectionTestUtils.setField(indicator, "id", ids.incrementAndGet());
            data.add(indicator);
            return indicator;
        }

        @Override
        public List<EnvironmentalIndicator> findByOrganizationId(Long organizationId) {
            return data.stream().filter(indicator -> indicator.getOrganizationId().equals(organizationId)).toList();
        }

        @Override
        public long count() {
            return data.size();
        }

        /**
         * Organization 1 with January (no Scope 3), February and March. April has no results.
         */
        public static Indicators withDemoData() {
            var indicators = new Indicators();
            indicators.add("2026-01", EmissionScope.SCOPE_1, 42.50);
            indicators.add("2026-01", EmissionScope.SCOPE_2, 30.00);
            indicators.add("2026-02", EmissionScope.SCOPE_1, 40.00);
            indicators.add("2026-02", EmissionScope.SCOPE_2, 28.50);
            indicators.add("2026-02", EmissionScope.SCOPE_3, 15.00);
            indicators.add("2026-03", EmissionScope.SCOPE_1, 44.20);
            indicators.add("2026-03", EmissionScope.SCOPE_2, 29.10);
            indicators.add("2026-03", EmissionScope.SCOPE_3, 16.30);
            return indicators;
        }

        public Indicators add(String period, EmissionScope scope, double tonnes) {
            save(new EnvironmentalIndicator(1L, scope, ReportingPeriod.of(period), Co2eTonnes.of(tonnes)));
            return this;
        }
    }

    public static final class Reports implements SustainabilityReportRepository {
        private final List<SustainabilityReport> data = new ArrayList<>();
        private final AtomicLong ids = new AtomicLong();

        @Override
        public Optional<SustainabilityReport> findById(Long id) {
            return data.stream().filter(report -> report.getId().equals(id)).findFirst();
        }

        @Override
        public List<SustainabilityReport> findByOrganizationId(Long organizationId) {
            return data.stream().filter(report -> report.getOrganizationId().equals(organizationId)).toList();
        }

        @Override
        public SustainabilityReport save(SustainabilityReport report) {
            if (report.getId() == null) {
                ReflectionTestUtils.setField(report, "id", ids.incrementAndGet());
                data.add(report);
            }
            return report;
        }
    }
}
