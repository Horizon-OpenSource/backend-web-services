package com.horizon.ecotrack.platform.carbonfootprint.application;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.EmissionFactorRepository;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.FootprintCalculationRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryRepositories {
    private InMemoryRepositories() {}

    public static class Factors implements EmissionFactorRepository {
        private final Map<String, EmissionFactor> store = new LinkedHashMap<>();
        public Optional<EmissionFactor> findById(String id) { return Optional.ofNullable(store.get(id)); }
        public List<EmissionFactor> findAll(FootprintScope scope) {
            return store.values().stream().filter(f -> scope == null || f.getScope() == scope).toList();
        }
        public EmissionFactor save(EmissionFactor factor) { store.put(factor.getId(), factor); return factor; }
        public void delete(EmissionFactor factor) { store.remove(factor.getId()); }
    }

    public static class Calculations implements FootprintCalculationRepository {
        private final Map<String, FootprintCalculation> store = new LinkedHashMap<>();
        public Optional<FootprintCalculation> findById(String id) { return Optional.ofNullable(store.get(id)); }
        public List<FootprintCalculation> findAll(String organizationId, String period) {
            List<FootprintCalculation> found = new ArrayList<>(store.values().stream()
                    .filter(c -> c.getOrganizationId().equals(organizationId))
                    .filter(c -> period == null || c.getPeriod().value().equals(period)).toList());
            found.sort(Comparator.comparing(FootprintCalculation::getCalculatedAt).reversed());
            return found;
        }
        public FootprintCalculation save(FootprintCalculation calculation) { store.put(calculation.getId(), calculation); return calculation; }
        public void delete(FootprintCalculation calculation) { store.remove(calculation.getId()); }
    }
}