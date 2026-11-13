package com.horizon.ecotrack.platform.analytics.domain.model.commands;

import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;

public record GenerateReportByPeriodCommand(Long organizationId, ReportingPeriod period) {
}
