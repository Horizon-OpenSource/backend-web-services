package com.horizon.ecotrack.platform.emissions.infrastructure.persistence;

import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataEmissionSourceRepository extends JpaRepository<EmissionSource, String> {
    List<EmissionSource> findByScope(EmissionScope scope);
}
