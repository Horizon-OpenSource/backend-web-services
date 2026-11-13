package com.horizon.ecotrack.platform.analytics.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportStatus;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportType;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReportGeneratedEvent;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.DownloadReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateConsolidatedReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateReportByPeriodCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.ComparePeriodsQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetDashboardQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetReportsHistoryQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetSummaryQuery;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnalyticsServicesTest {
    private static final Long ORGANIZATION = 1L;
    private static final ReportingPeriod JANUARY = ReportingPeriod.of("2026-01");
    private static final ReportingPeriod FEBRUARY = ReportingPeriod.of("2026-02");
    private static final ReportingPeriod MARCH = ReportingPeriod.of("2026-03");
    private static final ReportingPeriod APRIL = ReportingPeriod.of("2026-04");

    private InMemoryAnalyticsRepositories.Reports reports;
    private List<Object> publishedEvents;
    private AnalyticsQueryService queries;
    private ReportCommandService commands;

    @BeforeEach
    void setUp() {
        var indicators = InMemoryAnalyticsRepositories.Indicators.withDemoData();
        reports = new InMemoryAnalyticsRepositories.Reports();
        publishedEvents = new ArrayList<>();
        queries = new AnalyticsQueryService(indicators, reports);
        commands = new ReportCommandService(indicators, reports, publishedEvents::add);
    }

    @Test
    void dashboardReturnsEveryIndicatorOfTheOrganization() {
        assertThat(queries.handle(new GetDashboardQuery(ORGANIZATION))).hasSize(8);
        assertThat(queries.handle(new GetDashboardQuery(99L))).isEmpty();
    }

    @Test
    void summaryWithoutPeriodListsEveryPeriodWithResultsInOrder() {
        var summaries = queries.handle(new GetSummaryQuery(ORGANIZATION, null));

        assertThat(summaries).extracting(summary -> summary.period().value())
                .containsExactly("2026-01", "2026-02", "2026-03");
        assertThat(summaries.get(0).total().orElseThrow().toDouble()).isEqualTo(72.5);
        assertThat(summaries.get(0).missingScopes()).hasSize(1);
    }

    @Test
    void summaryOfAPeriodWithoutResultsIsReturnedWithoutData() {
        var summaries = queries.handle(new GetSummaryQuery(ORGANIZATION, APRIL));

        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).hasData()).isFalse();
    }

    @Test
    void comparisonReturnsTheVariationBetweenTwoPeriods() {
        var comparison = queries.handle(new ComparePeriodsQuery(ORGANIZATION, FEBRUARY, MARCH));

        assertThat(comparison.variationTonnes()).isEqualByComparingTo("6.10");
        assertThat(comparison.variationPercent().orElseThrow()).isEqualByComparingTo("7.31");
    }

    @Test
    void comparisonFailsWhenAPeriodHasNoResults() {
        assertThatThrownBy(() -> queries.handle(new ComparePeriodsQuery(ORGANIZATION, FEBRUARY, APRIL)))
                .isInstanceOf(InsufficientDataException.class);
    }

    @Test
    void consolidatedReportAddsEveryPeriodWithResults() {
        var report = commands.handle(new GenerateConsolidatedReportCommand(ORGANIZATION));

        assertThat(report.getId()).isNotNull();
        assertThat(report.getType()).isEqualTo(ReportType.CONSOLIDATED);
        assertThat(report.getPeriod()).isNull();
        assertThat(report.getTotalTonnes().toDouble()).isEqualTo(245.6);
        assertThat(report.getStatus()).isEqualTo(ReportStatus.GENERATED);
        assertThat(publishedEvents).singleElement().isInstanceOf(SustainabilityReportGeneratedEvent.class);
    }

    @Test
    void consolidatedReportFailsWhenTheOrganizationHasNoResults() {
        assertThatThrownBy(() -> commands.handle(new GenerateConsolidatedReportCommand(99L)))
                .isInstanceOf(InsufficientDataException.class);
        assertThat(publishedEvents).isEmpty();
    }

    @Test
    void periodReportContainsOnlyThatPeriod() {
        var report = commands.handle(new GenerateReportByPeriodCommand(ORGANIZATION, FEBRUARY));

        assertThat(report.getType()).isEqualTo(ReportType.PERIOD);
        assertThat(report.getPeriod()).isEqualTo(FEBRUARY);
        assertThat(report.getTotalTonnes().toDouble()).isEqualTo(83.5);
    }

    @Test
    void periodReportFailsWhenThePeriodHasNoResults() {
        assertThatThrownBy(() -> commands.handle(new GenerateReportByPeriodCommand(ORGANIZATION, APRIL)))
                .isInstanceOf(InsufficientDataException.class);
    }

    @Test
    void historyIsSortedNewestFirstAndCanBeFilteredByPeriod() {
        reports.save(SustainabilityReport.forPeriod(ORGANIZATION, JANUARY, Co2eTonnes.of(72.5),
                LocalDateTime.of(2026, 11, 1, 9, 0)));
        reports.save(SustainabilityReport.forPeriod(ORGANIZATION, FEBRUARY, Co2eTonnes.of(83.5),
                LocalDateTime.of(2026, 11, 3, 9, 0)));
        reports.save(SustainabilityReport.consolidated(ORGANIZATION, Co2eTonnes.of(245.6),
                LocalDateTime.of(2026, 11, 2, 9, 0)));

        var all = queries.handle(new GetReportsHistoryQuery(ORGANIZATION, null));
        var february = queries.handle(new GetReportsHistoryQuery(ORGANIZATION, FEBRUARY));
        var none = queries.handle(new GetReportsHistoryQuery(ORGANIZATION, APRIL));

        assertThat(all).extracting(SustainabilityReport::getType)
                .containsExactly(ReportType.PERIOD, ReportType.CONSOLIDATED, ReportType.PERIOD);
        assertThat(all.get(0).getPeriod()).isEqualTo(FEBRUARY);
        assertThat(february).hasSize(1);
        assertThat(none).isEmpty();
    }

    @Test
    void downloadReturnsCsvAndMarksTheReportAsDownloaded() {
        var report = commands.handle(new GenerateReportByPeriodCommand(ORGANIZATION, FEBRUARY));

        var csv = new String(commands.handle(new DownloadReportCommand(report.getId())), StandardCharsets.UTF_8);

        assertThat(csv).startsWith("﻿Report," + report.getId());
        assertThat(csv).contains("Type,PERIOD", "Period,2026-02", "Total (t CO2e),83.50");
        assertThat(csv).contains("2026-02,SCOPE_1,40.00", "2026-02,SCOPE_3,15.00");
        assertThat(csv).doesNotContain("2026-03,");
        assertThat(report.getStatus()).isEqualTo(ReportStatus.DOWNLOADED);
    }

    @Test
    void consolidatedCsvIncludesEveryPeriodAndLeavesMissingScopesEmpty() {
        var report = commands.handle(new GenerateConsolidatedReportCommand(ORGANIZATION));

        var csv = new String(commands.handle(new DownloadReportCommand(report.getId())), StandardCharsets.UTF_8);

        assertThat(csv).contains("Period,ALL", "2026-01,SCOPE_1,42.50", "2026-01,SCOPE_3,", "2026-03,SCOPE_3,16.30");
    }

    @Test
    void downloadFailsWhenTheReportDoesNotExist() {
        assertThatThrownBy(() -> commands.handle(new DownloadReportCommand(999L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
