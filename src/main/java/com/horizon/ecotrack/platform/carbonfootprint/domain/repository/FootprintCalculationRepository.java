package com.horizon.ecotrack.platform.carbonfootprint.domain.repository;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import java.util.List;
import java.util.Optional;

public interface FootprintCalculationRepository {
    Optional<FootprintCalculation> findById(String id);
    List<FootprintCalculation> findAll(String organizationId, String period);
    FootprintCalculation save(FootprintCalculation calculation);
    void delete(FootprintCalculation calculation);
}