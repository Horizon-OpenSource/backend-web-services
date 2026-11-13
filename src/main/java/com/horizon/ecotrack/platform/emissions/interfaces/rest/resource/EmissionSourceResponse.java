package com.horizon.ecotrack.platform.emissions.interfaces.rest.resource;

import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import java.time.Instant;

public record EmissionSourceResponse(String id, String facilityId, String facilityName, String name,
                                     String type, EmissionScope scope, Instant createdAt) {
    public static EmissionSourceResponse from(EmissionSource source) {
        return new EmissionSourceResponse(source.getId(), source.getFacilityId(), source.getFacilityName(),
                source.getName(), source.getType(), source.getScope(), source.getCreatedAt());
    }
}
