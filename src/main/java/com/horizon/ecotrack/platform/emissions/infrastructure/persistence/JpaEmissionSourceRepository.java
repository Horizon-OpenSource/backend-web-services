package com.horizon.ecotrack.platform.emissions.infrastructure.persistence;

import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import com.horizon.ecotrack.platform.emissions.domain.repository.EmissionSourceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaEmissionSourceRepository implements EmissionSourceRepository {
    private final SpringDataEmissionSourceRepository data;
    JpaEmissionSourceRepository(SpringDataEmissionSourceRepository data) { this.data = data; }
    @Override public Optional<EmissionSource> findById(String id) { return data.findById(id); }
    @Override public List<EmissionSource> findAll(EmissionScope scope) {
        return scope == null ? data.findAll() : data.findByScope(scope);
    }
    @Override public EmissionSource save(EmissionSource source) { return data.save(source); }
    @Override public void delete(EmissionSource source) { data.delete(source); data.flush(); }
}
