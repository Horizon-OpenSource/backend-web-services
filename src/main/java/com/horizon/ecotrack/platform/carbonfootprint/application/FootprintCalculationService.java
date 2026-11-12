package com.horizon.ecotrack.platform.carbonfootprint.application;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ActivityLine;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ActivityQuantity;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.CarbonRules;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ReportingPeriod;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.EmissionFactorRepository;
import com.horizon.ecotrack.platform.carbonfootprint.domain.repository.FootprintCalculationRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FootprintCalculationService {
    private final FootprintCalculationRepository calculations;
    private final EmissionFactorRepository factors;

    public FootprintCalculationService(FootprintCalculationRepository calculations, EmissionFactorRepository factors) {
        this.calculations = calculations;
        this.factors = factors;
    }

    @Transactional
    public FootprintCalculation calculate(CalculateFootprintCommand command) {
        String organizationId = CarbonRules.text(command.organizationId(), "organizationId", 36);
        ReportingPeriod period = ReportingPeriod.of(command.period());
        if (command.items() == null || command.items().isEmpty()) {
            throw new IllegalArgumentException("at least one activity record is required to calculate");
        }
        List<ActivityLine> lines = new ArrayList<>();
        for (CalculateFootprintCommand.Item item : command.items()) {
            String factorId = CarbonRules.text(item.emissionFactorId(), "emissionFactorId", 36);
            EmissionFactor factor = factors.findById(factorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Emission factor not found: " + factorId));
            lines.add(new ActivityLine(item.activityDataId(), item.sourceName(), item.scope(),
                    new ActivityQuantity(item.quantity(), item.unit()), factor));
        }
        FootprintCalculation calculation = FootprintCalculation.calculate(organizationId, period, lines);
        calculations.findAll(organizationId, period.value()).forEach(calculations::delete);
        return calculations.save(calculation);
    }

    @Transactional(readOnly = true)
    public List<FootprintCalculation> list(String organizationId, String period) {
        return calculations.findAll(validOrganization(organizationId), period == null ? null : ReportingPeriod.of(period).value());
    }

    @Transactional(readOnly = true)
    public FootprintCalculation get(String id) {
        return calculations.findById(id).orElseThrow(() -> new ResourceNotFoundException("Footprint calculation not found"));
    }

    @Transactional(readOnly = true)
    public FootprintCalculation resultsOf(String organizationId, String period) {
        return calculations.findAll(validOrganization(organizationId), ReportingPeriod.of(period).value()).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No calculation found for period " + period));
    }

    @Transactional(readOnly = true)
    public List<FootprintCalculation> history(String organizationId) {
        return calculations.findAll(validOrganization(organizationId), null);
    }

    @Transactional
    public void delete(String id) { calculations.delete(get(id)); }

    private String validOrganization(String organizationId) {
        return CarbonRules.text(organizationId, "organizationId", 36);
    }
}