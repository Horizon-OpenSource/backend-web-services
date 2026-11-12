package com.horizon.ecotrack.platform.carbonfootprint.infrastructure.persistence;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.EmissionFactorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaEmissionFactorRepository implements EmissionFactorRepository {
    private final SpringDataEmissionFactorRepository data;
    JpaEmissionFactorRepository(SpringDataEmissionFactorRepository data) { this.data = data; }
    @Override public Optional<EmissionFactor> findById(String id) { return data.findById(id); }
    @Override public List<EmissionFactor> findAll(FootprintScope scope) {
        return scope == null ? data.findAll() : data.findByScope(scope);
    }
    @Override public EmissionFactor save(EmissionFactor factor) { return data.save(factor); }
    @Override public void delete(EmissionFactor factor) { data.delete(factor); }
}
