package com.horizon.ecotrack.platform.analytics.domain.model.queries;

import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;

public record ComparePeriodsQuery(Long organizationId, ReportingPeriod from, ReportingPeriod to) {
}
