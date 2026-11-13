package com.horizon.ecotrack.platform.analytics.domain.model;

import java.time.LocalDateTime;

public record SustainabilityReportGeneratedEvent(Long reportId, LocalDateTime occurredOn) {
}
