package com.horizon.ecotrack.platform.carbonfootprint.infrastructure.persistence;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataEmissionFactorRepository extends JpaRepository<EmissionFactor, String> {
    List<EmissionFactor> findByScope(FootprintScope scope);
}