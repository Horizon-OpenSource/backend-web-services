package com.horizon.ecotrack.platform.carbonfootprint.infrastructure.persistence;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataFootprintCalculationRepository extends JpaRepository<FootprintCalculation, String> {
    List<FootprintCalculation> findByOrganizationIdOrderByCalculatedAtDesc(String organizationId);
    List<FootprintCalculation> findByOrganizationIdAndPeriodOrderByCalculatedAtDesc(String organizationId, String period);
}