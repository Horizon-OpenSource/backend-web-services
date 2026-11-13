package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record FootprintCalculationResponse(String id, String organizationId, String period, Instant calculatedAt,
                                           BigDecimal total, List<CalculationItemResponse> items) {
    public static FootprintCalculationResponse from(FootprintCalculation calculation) {
        return new FootprintCalculationResponse(calculation.getId(), calculation.getOrganizationId(),
                calculation.getPeriod().value(), calculation.getCalculatedAt(), calculation.total().kgCo2e(),
                calculation.getItems().stream().map(CalculationItemResponse::from).toList());
    }
}