package com.horizon.ecotrack.platform.carbonfootprint.infrastructure.persistence;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.FootprintCalculationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaFootprintCalculationRepository implements FootprintCalculationRepository {
    private final SpringDataFootprintCalculationRepository data;
    JpaFootprintCalculationRepository(SpringDataFootprintCalculationRepository data) { this.data = data; }
    @Override public Optional<FootprintCalculation> findById(String id) { return data.findById(id); }
    @Override public List<FootprintCalculation> findAll(String organizationId, String period) {
        return period == null
                ? data.findByOrganizationIdOrderByCalculatedAtDesc(organizationId)
                : data.findByOrganizationIdAndPeriodOrderByCalculatedAtDesc(organizationId, period);
    }
    @Override public FootprintCalculation save(FootprintCalculation calculation) { return data.save(calculation); }
    @Override public void delete(FootprintCalculation calculation) { data.delete(calculation); }
}
