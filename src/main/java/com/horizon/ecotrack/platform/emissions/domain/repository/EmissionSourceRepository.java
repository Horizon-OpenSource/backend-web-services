package com.horizon.ecotrack.platform.emissions.domain.repository;

import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import java.util.List;
import java.util.Optional;

/** Persistence port for the emission source aggregate. */
public interface EmissionSourceRepository {
    Optional<EmissionSource> findById(String id);
    List<EmissionSource> findAll(EmissionScope scope);
    EmissionSource save(EmissionSource source);
    void delete(EmissionSource source);
}
