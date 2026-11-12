package com.horizon.ecotrack.platform.carbonfootprint.application;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;
import java.util.List;

/** Command to calculate (or recalculate) the footprint of one reporting period. */
public record CalculateFootprintCommand(String organizationId, String period, List<Item> items) {
    /** One activity record with the emission factor selected for it. */
    public record Item(String activityDataId, String sourceName, FootprintScope scope,
                       BigDecimal quantity, String unit, String emissionFactorId) {}
}
