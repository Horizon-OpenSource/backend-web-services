package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import java.math.BigDecimal;
import java.time.Instant;

public record HistoryEntryResponse(String calculationId, String period, Instant calculatedAt, int records, BigDecimal total) {
    public static HistoryEntryResponse from(FootprintCalculation calculation) {
        return new HistoryEntryResponse(calculation.getId(), calculation.getPeriod().value(),
                calculation.getCalculatedAt(), calculation.getItems().size(), calculation.total().kgCo2e());
    }
}
