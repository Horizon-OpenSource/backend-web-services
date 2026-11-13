package com.horizon.ecotrack.platform.analytics.application;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.model.PeriodSummary;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportType;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReportGeneratedEvent;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.DownloadReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateConsolidatedReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateReportByPeriodCommand;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import com.horizon.ecotrack.platform.analytics.domain.repository.SustainabilityReportRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportCommandService {
    private static final String CSV_BOM = "﻿";

    private final EnvironmentalIndicatorRepository indicators;
    private final SustainabilityReportRepository reports;
    private final ApplicationEventPublisher events;

    public ReportCommandService(EnvironmentalIndicatorRepository indicators, SustainabilityReportRepository reports,
                                ApplicationEventPublisher events) {
        this.indicators = indicators;
        this.reports = reports;
        this.events = events;
    }

    @Transactional
    public SustainabilityReport handle(GenerateConsolidatedReportCommand command) {
        var summaries = summariesWithData(indicators.findByOrganizationId(command.organizationId()));
        if (summaries.isEmpty()) {
            throw new InsufficientDataException("There is no environmental information to generate the report");
        }
        var report = SustainabilityReport.consolidated(command.organizationId(), totalOf(summaries),
                LocalDateTime.now());
        return saveAndAnnounce(report);
    }

    @Transactional
    public SustainabilityReport handle(GenerateReportByPeriodCommand command) {
        var summary = PeriodSummary.from(command.period(), indicators.findByOrganizationId(command.organizationId()));
        if (!summary.hasData()) {
            throw new InsufficientDataException("There is not enough data to generate a report for %s"
                    .formatted(command.period()));
        }
        var report = SustainabilityReport.forPeriod(command.organizationId(), command.period(),
                summary.total().orElseThrow(), LocalDateTime.now());
        return saveAndAnnounce(report);
    }

    @Transactional
    public byte[] handle(DownloadReportCommand command) {
        var report = reports.findById(command.reportId())
                .orElseThrow(() -> new ResourceNotFoundException("Report " + command.reportId() + " not found"));
        var content = csvOf(report, indicators.findByOrganizationId(report.getOrganizationId()));
        report.markAsDownloaded();
        reports.save(report);
        return content.getBytes(StandardCharsets.UTF_8);
    }

    private SustainabilityReport saveAndAnnounce(SustainabilityReport report) {
        var saved = reports.save(report);
        events.publishEvent(new SustainabilityReportGeneratedEvent(saved.getId(), LocalDateTime.now()));
        return saved;
    }

    private static List<PeriodSummary> summariesWithData(List<EnvironmentalIndicator> all) {
        return all.stream()
                .map(EnvironmentalIndicator::getPeriod)
                .distinct()
                .sorted()
                .map(period -> PeriodSummary.from(period, all))
                .filter(PeriodSummary::hasData)
                .toList();
    }

    private static Co2eTonnes totalOf(List<PeriodSummary> summaries) {
        return summaries.stream()
                .map(summary -> summary.total().orElseThrow())
                .reduce(Co2eTonnes.ZERO, Co2eTonnes::plus);
    }

    private static String csvOf(SustainabilityReport report, List<EnvironmentalIndicator> all) {
        var summaries = summariesWithData(all).stream()
                .filter(summary -> report.getType() == ReportType.CONSOLIDATED
                        || summary.period().equals(report.getPeriod()))
                .toList();
        var csv = new StringBuilder(CSV_BOM);
        csv.append("Report,").append(report.getId()).append('\n');
        csv.append("Type,").append(report.getType()).append('\n');
        csv.append("Period,").append(report.getPeriod() == null ? "ALL" : report.getPeriod()).append('\n');
        csv.append("Generated at,").append(report.getGeneratedAt()).append('\n');
        csv.append("Total (t CO2e),").append(report.getTotalTonnes()).append('\n');
        csv.append('\n');
        csv.append("Period,Scope,Tonnes CO2e\n");
        for (var summary : summaries) {
            for (var scope : summary.scopes()) {
                csv.append(summary.period()).append(',').append(scope.scope()).append(',')
                        .append(scope.hasData() ? scope.tonnes().toString() : "").append('\n');
            }
        }
        return csv.toString();
    }
}
