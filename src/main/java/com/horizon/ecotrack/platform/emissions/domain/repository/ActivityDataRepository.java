package com.horizon.ecotrack.platform.emissions.domain.repository;

import com.horizon.ecotrack.platform.emissions.domain.model.ActivityData;
import java.util.List;
import java.util.Optional;

/** Persistence port for activity records. */
public interface ActivityDataRepository {
    Optional<ActivityData> findById(String id);
    List<ActivityData> findAll(String sourceId);
    boolean existsForSource(String sourceId);
    ActivityData save(ActivityData data);
    void delete(ActivityData data);
}
