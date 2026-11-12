package com.horizon.ecotrack.platform.carbonfootprint.application;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.EmissionFactorRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmissionFactorService {
    private final EmissionFactorRepository factors;

    public EmissionFactorService(EmissionFactorRepository factors) { this.factors = factors; }

    @Transactional(readOnly = true)
    public List<EmissionFactor> list(FootprintScope scope) { return factors.findAll(scope); }

    @Transactional(readOnly = true)
    public EmissionFactor get(String id) {
        return factors.findById(id).orElseThrow(() -> new ResourceNotFoundException("Emission factor not found"));
    }

    @Transactional
    public EmissionFactor create(String name, FootprintScope scope, String unit, BigDecimal value, String source) {
        return factors.save(new EmissionFactor(name, scope, unit, value, source));
    }

    @Transactional
    public EmissionFactor update(String id, String name, FootprintScope scope, String unit, BigDecimal value, String source) {
        EmissionFactor factor = get(id);
        factor.changeDetails(name, scope, unit, value, source);
        return factors.save(factor);
    }

    @Transactional
    public void delete(String id) { factors.delete(get(id)); }
}
