package com.horizon.ecotrack.platform.carbonfootprint.domain.repository;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import java.util.List;
import java.util.Optional;

/** Persistence port for footprint calculations. */
public interface FootprintCalculationRepository {
    Optional<FootprintCalculation> findById(String id);
    /** Calculations of an organization, newest first; {@code period} may be null to include every period. */
    List<FootprintCalculation> findAll(String organizationId, String period);
    FootprintCalculation save(FootprintCalculation calculation);
    void delete(FootprintCalculation calculation);
}
