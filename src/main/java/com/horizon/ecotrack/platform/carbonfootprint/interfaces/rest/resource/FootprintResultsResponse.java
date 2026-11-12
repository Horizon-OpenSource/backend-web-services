package com.horizon.ecotrack.platform.carbonfootprint.interfaces.rest.resource;

import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintCalculation;
import com.horizon.ecotrack.platform.carbonfootprint.domain.model.FootprintScope;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Total and per-scope results of one calculated period. */
public record FootprintResultsResponse(String calculationId, String period, Instant calculatedAt, BigDecimal total,
                                       List<ScopeResultResponse> scopes) {
    public record ScopeResultResponse(FootprintScope scope, BigDecimal total, int records) {}

    public static FootprintResultsResponse from(FootprintCalculation calculation) {
        return new FootprintResultsResponse(calculation.getId(), calculation.getPeriod().value(),
                calculation.getCalculatedAt(), calculation.total().kgCo2e(),
                calculation.resultsByScope().stream()
                        .map(r -> new ScopeResultResponse(r.scope(), r.total().kgCo2e(), r.records())).toList());
    }
}
