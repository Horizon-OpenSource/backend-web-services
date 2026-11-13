package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.SustainabilityReport;
import com.horizon.ecotrack.platform.analytics.domain.repository.SustainabilityReportRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaSustainabilityReportRepository implements SustainabilityReportRepository {
    private final SpringDataSustainabilityReportRepository data;

    JpaSustainabilityReportRepository(SpringDataSustainabilityReportRepository data) {
        this.data = data;
    }

    @Override
    public Optional<SustainabilityReport> findById(Long id) {
        return data.findById(id);
    }

    @Override
    public List<SustainabilityReport> findByOrganizationId(Long organizationId) {
        return data.findByOrganizationIdOrderByGeneratedAtDesc(organizationId);
    }

    @Override
    public SustainabilityReport save(SustainabilityReport report) {
        return data.save(report);
    }
}
