package com.horizon.ecotrack.platform.carbonfootprint.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FootprintCalculationServiceTest {
    private InMemoryRepositories.Factors factors;
    private InMemoryRepositories.Calculations calculations;
    private FootprintCalculationService service;
    private String gasId;

    @BeforeEach
    void setUp() {
        factors = new InMemoryRepositories.Factors();
        calculations = new InMemoryRepositories.Calculations();
        service = new FootprintCalculationService(calculations, factors);
        gasId = new EmissionFactorService(factors).create("Natural gas", FootprintScope.SCOPE_1, "m3", new BigDecimal("2.0"), "Demo").getId();
    }

    private CalculateFootprintCommand command(String period, String quantity) {
        return new CalculateFootprintCommand("1", period, List.of(new CalculateFootprintCommand.Item(
                "a1", "Boiler", FootprintScope.SCOPE_1, new BigDecimal(quantity), "m3", gasId)));
    }

    @Test
    void calculatesAndKeepsHistoryPerPeriod() {
        service.calculate(command("2026-09", "10"));
        service.calculate(command("2026-10", "20"));
        assertEquals(2, service.history("1").size());
        assertEquals(0, new BigDecimal("40").compareTo(service.resultsOf("1", "2026-10").total().kgCo2e()));
    }

    @Test
    void recalculatingSamePeriodReplacesPreviousResult() {
        service.calculate(command("2026-10", "10"));
        service.calculate(command("2026-10", "30"));
        assertEquals(1, service.list("1", "2026-10").size());
        assertEquals(0, new BigDecimal("60").compareTo(service.resultsOf("1", "2026-10").total().kgCo2e()));
    }

    @Test
    void failsForUnknownFactorOrPeriodWithoutCalculation() {
        var unknown = new CalculateFootprintCommand("1", "2026-10", List.of(new CalculateFootprintCommand.Item(
                "a1", "Boiler", FootprintScope.SCOPE_1, BigDecimal.TEN, "m3", "missing")));
        assertThrows(ResourceNotFoundException.class, () -> service.calculate(unknown));
        assertThrows(ResourceNotFoundException.class, () -> service.resultsOf("1", "2026-01"));
        assertThrows(IllegalArgumentException.class, () -> service.calculate(command("bad", "10")));
    }

    @Test
    void deleteRemovesCalculation() {
        var created = service.calculate(command("2026-10", "10"));
        service.delete(created.getId());
        assertThrows(ResourceNotFoundException.class, () -> service.get(created.getId()));
    }
}
