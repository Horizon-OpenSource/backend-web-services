package com.horizon.ecotrack.platform.analytics.interfaces.rest;

import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.interfaces.rest.resource.SustainabilityReportResource;

final class SustainabilityReportResourceAssembler {
    private SustainabilityReportResourceAssembler() {
    }

    static SustainabilityReportResource toResourceFrom(SustainabilityReport report) {
        return new SustainabilityReportResource(report.getId(), report.getOrganizationId(), report.getType().name(),
                report.getPeriod() == null ? null : report.getPeriod().value(),
                report.getTotalTonnes().toDouble(), report.getStatus().name(), report.getGeneratedAt());
    }
}
