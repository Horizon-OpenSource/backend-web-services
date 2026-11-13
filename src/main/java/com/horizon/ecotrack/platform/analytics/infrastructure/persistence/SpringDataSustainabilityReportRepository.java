package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataSustainabilityReportRepository extends JpaRepository<SustainabilityReport, Long> {
    List<SustainabilityReport> findByOrganizationIdOrderByGeneratedAtDesc(Long organizationId);
}
