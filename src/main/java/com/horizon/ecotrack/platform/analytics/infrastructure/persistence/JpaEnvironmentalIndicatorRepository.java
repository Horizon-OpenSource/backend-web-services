package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import com.horizon.ecotrack.platform.analytics.domain.repository.EnvironmentalIndicatorRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class JpaEnvironmentalIndicatorRepository implements EnvironmentalIndicatorRepository {
    private final SpringDataEnvironmentalIndicatorRepository data;

    JpaEnvironmentalIndicatorRepository(SpringDataEnvironmentalIndicatorRepository data) {
        this.data = data;
    }

    @Override
    public EnvironmentalIndicator save(EnvironmentalIndicator indicator) {
        return data.save(indicator);
    }

    @Override
    public List<EnvironmentalIndicator> findByOrganizationId(Long organizationId) {
        return data.findByOrganizationIdOrderByPeriodAscTypeAsc(organizationId);
    }

    @Override
    public long count() {
        return data.count();
    }
}
