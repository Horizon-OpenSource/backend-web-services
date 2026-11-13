package com.horizon.ecotrack.platform.analytics.domain.repository;

import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import java.util.List;
import java.util.Optional;

public interface SustainabilityReportRepository {
    Optional<SustainabilityReport> findById(Long id);

    List<SustainabilityReport> findByOrganizationId(Long organizationId);

    SustainabilityReport save(SustainabilityReport report);
}
