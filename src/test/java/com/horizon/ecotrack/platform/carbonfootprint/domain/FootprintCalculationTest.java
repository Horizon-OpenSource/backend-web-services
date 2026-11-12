package com.horizon.ecotrack.platform.carbonfootprint.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ActivityLine;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ActivityQuantity;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.ReportingPeriod;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class FootprintCalculationTest {
    private final EmissionFactor gas = new EmissionFactor("Natural gas", FootprintScope.SCOPE_1, "m3", new BigDecimal("2.0"), "Demo");
    private final EmissionFactor grid = new EmissionFactor("Grid electricity", FootprintScope.SCOPE_2, "kWh", new BigDecimal("0.25"), "Demo");

    private ActivityLine line(String id, FootprintScope scope, String amount, String unit, EmissionFactor factor) {
        return new ActivityLine(id, "Source " + id, scope, new ActivityQuantity(new BigDecimal(amount), unit), factor);
    }

    @Test
    void multipliesQuantityByFactorAndGroupsByScope() {
        var calculation = FootprintCalculation.calculate("1", ReportingPeriod.of("2026-10"), List.of(
                line("a1", FootprintScope.SCOPE_1, "100", "m3", gas),
                line("a2", FootprintScope.SCOPE_1, "50", "M3", gas),
                line("a3", FootprintScope.SCOPE_2, "1000", "kWh", grid)));

        assertEquals(0, new BigDecimal("550").compareTo(calculation.total().kgCo2e()));
        var scope1 = calculation.resultFor(FootprintScope.SCOPE_1);
        assertEquals(0, new BigDecimal("300").compareTo(scope1.total().kgCo2e()));
        assertEquals(2, scope1.records());
        assertEquals(0, new BigDecimal("250").compareTo(calculation.resultFor(FootprintScope.SCOPE_2).total().kgCo2e()));
        var scope3 = calculation.resultFor(FootprintScope.SCOPE_3);
        assertEquals(0, scope3.total().kgCo2e().signum());
        assertEquals(0, scope3.records());
        assertEquals(3, calculation.resultsByScope().size());
    }

    @Test
    void rejectsFactorOfAnotherScope() {
        assertThrows(IllegalArgumentException.class, () -> FootprintCalculation.calculate("1", ReportingPeriod.of("2026-10"),
                List.of(line("a1", FootprintScope.SCOPE_2, "10", "m3", gas))));
    }

    @Test
    void rejectsUnitMismatch() {
        assertThrows(IllegalArgumentException.class, () -> FootprintCalculation.calculate("1", ReportingPeriod.of("2026-10"),
                List.of(line("a1", FootprintScope.SCOPE_1, "10", "L", gas))));
    }

    @Test
    void rejectsRepeatedActivityAndEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> FootprintCalculation.calculate("1", ReportingPeriod.of("2026-10"),
                List.of(line("a1", FootprintScope.SCOPE_1, "10", "m3", gas), line("a1", FootprintScope.SCOPE_1, "5", "m3", gas))));
        assertThrows(IllegalArgumentException.class, () -> FootprintCalculation.calculate("1", ReportingPeriod.of("2026-10"), List.of()));
    }

    @Test
    void validatesValueObjects() {
        assertThrows(IllegalArgumentException.class, () -> ReportingPeriod.of("2026-13"));
        assertThrows(IllegalArgumentException.class, () -> ReportingPeriod.of("0000-01"));
        assertThrows(IllegalArgumentException.class, () -> ReportingPeriod.of("2026-1"));
        assertThrows(IllegalArgumentException.class, () -> new ActivityQuantity(BigDecimal.ZERO, "kWh"));
        assertThrows(IllegalArgumentException.class, () -> new EmissionFactor("X", FootprintScope.SCOPE_1, "kWh", new BigDecimal("-1"), "Demo"));
    }
}
