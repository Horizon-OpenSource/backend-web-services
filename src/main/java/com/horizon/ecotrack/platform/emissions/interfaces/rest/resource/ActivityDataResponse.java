package com.horizon.ecotrack.platform.emissions.interfaces.rest.resource;

import com.horizon.ecotrack.platform.emissions.domain.model.ActivityData;
import com.horizon.ecotrack.platform.emissions.domain.model.ActivityOrigin;
import java.math.BigDecimal;
import java.time.Instant;

public record ActivityDataResponse(String id, String emissionSourceId, String period, BigDecimal quantity,
                                   String unit, ActivityOrigin origin, Instant recordedAt) {
    public static ActivityDataResponse from(ActivityData activity) {
        return new ActivityDataResponse(activity.getId(), activity.getSource().getId(), activity.getPeriod(),
                activity.getQuantity(), activity.getUnit(), activity.getOrigin(), activity.getRecordedAt());
    }
}
