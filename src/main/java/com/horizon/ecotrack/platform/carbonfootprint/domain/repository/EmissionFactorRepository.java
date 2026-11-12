package com.horizon.ecotrack.platform.carbonfootprint.domain.repository;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.util.List;
import java.util.Optional;

/** Persistence port for emission factors. */
public interface EmissionFactorRepository {
    Optional<EmissionFactor> findById(String id);
    List<EmissionFactor> findAll(FootprintScope scope);
    EmissionFactor save(EmissionFactor factor);
    void delete(EmissionFactor factor);
}
