package com.horizon.ecotrack.platform.carbonfootprint.application;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;
import java.util.List;

public record CalculateFootprintCommand(String organizationId, String period, List<Item> items) {
    public record Item(String activityDataId, String sourceName, FootprintScope scope,
                       BigDecimal quantity, String unit, String emissionFactorId) {}
}