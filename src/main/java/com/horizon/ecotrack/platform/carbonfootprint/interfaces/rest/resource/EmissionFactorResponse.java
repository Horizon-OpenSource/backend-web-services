package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.EmissionFactor;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;

public record EmissionFactorResponse(String id, String name, FootprintScope scope, String unit,
                                     BigDecimal value, String source) {
    public static EmissionFactorResponse from(EmissionFactor factor) {
        return new EmissionFactorResponse(factor.getId(), factor.getName(), factor.getScope(),
                factor.getUnit(), factor.getValue(), factor.getSource());
    }
}
