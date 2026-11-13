package com.horizon.ecotrack.platform.carbonfootprint.infrastructure.seed;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.EmissionFactorRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "ecotrack.carbon-footprint.demo-data", havingValue = "true")
class EmissionFactorDemoDataLoader implements ApplicationRunner {
    private static final String SOURCE = "Illustrative demo value";
    private final EmissionFactorRepository factors;

    EmissionFactorDemoDataLoader(EmissionFactorRepository factors) { this.factors = factors; }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!factors.findAll(null).isEmpty()) return;
        List.of(
                new EmissionFactor("Natural gas combustion", FootprintScope.SCOPE_1, "m3", new BigDecimal("2.000000"), SOURCE),
                new EmissionFactor("Diesel combustion", FootprintScope.SCOPE_1, "L", new BigDecimal("2.680000"), SOURCE),
                new EmissionFactor("Grid electricity", FootprintScope.SCOPE_2, "kWh", new BigDecimal("0.250000"), SOURCE),
                new EmissionFactor("Air travel", FootprintScope.SCOPE_3, "km", new BigDecimal("0.150000"), SOURCE),
                new EmissionFactor("Road freight", FootprintScope.SCOPE_3, "t-km", new BigDecimal("0.100000"), SOURCE)
        ).forEach(factors::save);
    }
}