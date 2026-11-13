package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.CalculationItem;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;

public record CalculationItemResponse(String id, String activityDataId, String sourceName, FootprintScope scope,
                                      BigDecimal quantity, String unit, String emissionFactorId, String factorName,
                                      BigDecimal factorValue, BigDecimal emissions) {
    public static CalculationItemResponse from(CalculationItem item) {
        return new CalculationItemResponse(item.getId(), item.getActivityDataId(), item.getSourceName(), item.getScope(),
                item.getQuantity(), item.getUnit(), item.getEmissionFactorId(), item.getFactorName(),
                item.getFactorValue(), item.getEmissions().kgCo2e());
    }
}