package com.horizon.ecotrack.platform.analytics.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Environmental report of an organization. A consolidated report covers every period with results and
 * has no period; a period report covers one period.
 */
@Entity
@Table(name = "sustainability_reports")
public class SustainabilityReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ReportType type;

    @Column(name = "period", length = 20)
    private ReportingPeriod period;

    @Column(name = "total_tonnes", nullable = false, precision = 14, scale = 2)
    private Co2eTonnes totalTonnes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReportStatus status;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    protected SustainabilityReport() {
    }

    private SustainabilityReport(Long organizationId, ReportType type, ReportingPeriod period,
                                 Co2eTonnes totalTonnes, LocalDateTime generatedAt) {
        if (organizationId == null || organizationId <= 0) {
            throw new IllegalArgumentException("organizationId must be a positive number");
        }
        if (totalTonnes == null || generatedAt == null) {
            throw new IllegalArgumentException("totalTonnes and generatedAt are required");
        }
        this.organizationId = organizationId;
        this.type = type;
        this.period = period;
        this.totalTonnes = totalTonnes;
        this.generatedAt = generatedAt;
        this.status = ReportStatus.GENERATED;
    }

    public static SustainabilityReport consolidated(Long organizationId, Co2eTonnes totalTonnes,
                                                    LocalDateTime generatedAt) {
        return new SustainabilityReport(organizationId, ReportType.CONSOLIDATED, null, totalTonnes, generatedAt);
    }

    public static SustainabilityReport forPeriod(Long organizationId, ReportingPeriod period,
                                                 Co2eTonnes totalTonnes, LocalDateTime generatedAt) {
        if (period == null) {
            throw new IllegalArgumentException("period is required for a period report");
        }
        return new SustainabilityReport(organizationId, ReportType.PERIOD, period, totalTonnes, generatedAt);
    }

    public void markAsDownloaded() {
        this.status = ReportStatus.DOWNLOADED;
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public ReportType getType() {
        return type;
    }

    public ReportingPeriod getPeriod() {
        return period;
    }

    public Co2eTonnes getTotalTonnes() {
        return totalTonnes;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
