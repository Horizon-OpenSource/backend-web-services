package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.EnvironmentalIndicator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataEnvironmentalIndicatorRepository extends JpaRepository<EnvironmentalIndicator, Long> {
    List<EnvironmentalIndicator> findByOrganizationIdOrderByPeriodAscTypeAsc(Long organizationId);
}
