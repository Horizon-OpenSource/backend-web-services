package com.horizon.ecotrack.platform.analytics.domain.repository;

import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import java.util.List;

public interface EnvironmentalIndicatorRepository {
    EnvironmentalIndicator save(EnvironmentalIndicator indicator);

    List<EnvironmentalIndicator> findByOrganizationId(Long organizationId);

    long count();
}
