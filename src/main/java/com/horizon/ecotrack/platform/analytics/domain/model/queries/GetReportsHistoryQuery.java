package com.horizon.ecotrack.platform.analytics.domain.model.queries;

import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;

/**
 * Reports of an organization, newest first. When the period is not null, only reports of that period are returned.
 */
public record GetReportsHistoryQuery(Long organizationId, ReportingPeriod period) {
}
