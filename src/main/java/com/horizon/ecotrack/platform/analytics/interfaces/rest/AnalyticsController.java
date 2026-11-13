package com.horizon.ecotrack.platform.analytics.interfaces.rest;

import com.horizon.ecotrack.platform.analytics.application.AnalyticsQueryService;
import com.horizon.ecotrack.platform.analytics.application.ReportCommandService;
import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.DownloadReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateConsolidatedReportCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.commands.GenerateReportByPeriodCommand;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.ComparePeriodsQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetDashboardQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetReportsHistoryQuery;
import com.horizon.ecotrack.platform.analytics.domain.model.queries.GetSummaryQuery;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.EnvironmentalIndicatorResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.GenerateConsolidatedReportRequest;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.GenerateReportByPeriodRequest;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.PeriodComparisonResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.PeriodSummaryResource;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.SustainabilityReportResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Analytics and reports",
        description = "Environmental indicators, period comparison and sustainability reports")
public class AnalyticsController {
    private final AnalyticsQueryService queryService;
    private final ReportCommandService commandService;

    public AnalyticsController(AnalyticsQueryService queryService, ReportCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get every indicator of an organization",
            description = "Returns the emissions of each scope and period, ordered by period and scope.")
    public List<EnvironmentalIndicatorResource> getDashboard(@RequestParam("organizationId") Long organizationId) {
        return queryService.handle(new GetDashboardQuery(organizationId)).stream()
                .map(AnalyticsResourceAssembler::toResourceFrom)
                .toList();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get the total and the emissions by scope of each period",
            description = "Without a period, every period with results is returned. With a period, a summary is "
                    + "always returned and hasData is false when the period has no results.")
    public List<PeriodSummaryResource> getSummary(@RequestParam("organizationId") Long organizationId,
                                                  @RequestParam(value = "period", required = false) String period) {
        var query = new GetSummaryQuery(organizationId, period == null ? null : ReportingPeriod.of(period));
        return queryService.handle(query).stream().map(AnalyticsResourceAssembler::toResourceFrom).toList();
    }

    @GetMapping("/comparison")
    @Operation(summary = "Compare the emissions of two periods")
    @ApiResponse(responseCode = "422", description = "One of the periods has no results")
    public PeriodComparisonResource comparePeriods(@RequestParam("organizationId") Long organizationId,
                                                   @RequestParam("from") String from,
                                                   @RequestParam("to") String to) {
        var query = new ComparePeriodsQuery(organizationId, ReportingPeriod.of(from), ReportingPeriod.of(to));
        return AnalyticsResourceAssembler.toResourceFrom(queryService.handle(query));
    }

    @PostMapping("/reports/consolidated")
    @Operation(summary = "Generate a consolidated report with every period that has results")
    @ApiResponse(responseCode = "422", description = "The organization has no results to report")
    public ResponseEntity<SustainabilityReportResource> generateConsolidatedReport(
            @Valid @RequestBody GenerateConsolidatedReportRequest request) {
        var report = commandService.handle(new GenerateConsolidatedReportCommand(request.organizationId()));
        return created(SustainabilityReportResourceAssembler.toResourceFrom(report));
    }

    @PostMapping("/reports/period")
    @Operation(summary = "Generate a report for one period")
    @ApiResponse(responseCode = "422", description = "The period has no results to report")
    public ResponseEntity<SustainabilityReportResource> generateReportByPeriod(
            @Valid @RequestBody GenerateReportByPeriodRequest request) {
        var command = new GenerateReportByPeriodCommand(request.organizationId(),
                ReportingPeriod.of(request.period()));
        return created(SustainabilityReportResourceAssembler.toResourceFrom(commandService.handle(command)));
    }

    @GetMapping("/reports")
    @Operation(summary = "List the reports of an organization, newest first, optionally for one period")
    public List<SustainabilityReportResource> getReportsHistory(
            @RequestParam("organizationId") Long organizationId,
            @RequestParam(value = "period", required = false) String period) {
        var query = new GetReportsHistoryQuery(organizationId, period == null ? null : ReportingPeriod.of(period));
        return queryService.handle(query).stream()
                .map(SustainabilityReportResourceAssembler::toResourceFrom)
                .toList();
    }

    @GetMapping("/reports/{reportId}/download")
    @Operation(summary = "Download a report as a CSV file", description = "Marks the report as DOWNLOADED.")
    @ApiResponse(responseCode = "404", description = "The report is not available")
    public ResponseEntity<byte[]> downloadReport(@PathVariable("reportId") Long reportId) {
        byte[] csv = commandService.handle(new DownloadReportCommand(reportId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("sustainability-report-" + reportId + ".csv").build().toString())
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv);
    }

    private static ResponseEntity<SustainabilityReportResource> created(SustainabilityReportResource resource) {
        return ResponseEntity.created(URI.create("/api/v1/analytics/reports/" + resource.id())).body(resource);
    }
}
