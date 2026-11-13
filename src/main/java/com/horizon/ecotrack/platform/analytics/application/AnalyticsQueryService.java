package com.horizon.ecotrack.platform.analytics.application;

import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodComparison;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodSummary;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.ComparePeriodsQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetDashboardQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetReportsHistoryQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetSummaryQuery;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import com.horizon.ecotrack.platform.analytics.domain.repository.SustainabilityReportRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnalyticsQueryService {
    private final EnvironmentalIndicatorRepository indicators;
    private final SustainabilityReportRepository reports;

    public AnalyticsQueryService(EnvironmentalIndicatorRepository indicators, SustainabilityReportRepository reports) {
        this.indicators = indicators;
        this.reports = reports;
    }

    public List<EnvironmentalIndicator> handle(GetDashboardQuery query) {
        return indicators.findByOrganizationId(query.organizationId());
    }

    public List<PeriodSummary> handle(GetSummaryQuery query) {
        var all = indicators.findByOrganizationId(query.organizationId());
        if (query.period() != null) {
            return List.of(PeriodSummary.from(query.period(), all));
        }
        return all.stream()
                .map(EnvironmentalIndicator::getPeriod)
                .distinct()
                .sorted()
                .map(period -> PeriodSummary.from(period, all))
                .toList();
    }

    public PeriodComparison handle(ComparePeriodsQuery query) {
        var all = indicators.findByOrganizationId(query.organizationId());
        var from = PeriodSummary.from(query.from(), all);
        var to = PeriodSummary.from(query.to(), all);
        if (!from.hasData() || !to.hasData()) {
            throw new InsufficientDataException("There is not enough data to compare %s and %s"
                    .formatted(query.from(), query.to()));
        }
        return new PeriodComparison(from, to);
    }

    public List<SustainabilityReport> handle(GetReportsHistoryQuery query) {
        ReportingPeriod period = query.period();
        return reports.findByOrganizationId(query.organizationId()).stream()
                .filter(report -> period == null || period.equals(report.getPeriod()))
                .sorted(Comparator.comparing(SustainabilityReport::getGeneratedAt).reversed())
                .toList();
    }
}
