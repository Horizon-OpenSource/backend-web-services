package com.horizon.ecotrack.platform.analytics.domain.model.queries;

import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;

/**
 * Summary of an organization. When the period is null, the summary of every period with results is returned.
 */
public record GetSummaryQuery(Long organizationId, ReportingPeriod period) {
}
