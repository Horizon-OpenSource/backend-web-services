package com.horizon.ecotrack.platform.emissions.infrastructure.persistence;

import com.horizon.ecotrack.platform.emissions.domain.model.ActivityData;
import com.horizon.ecotrack.platform.emissions.domain.repository.ActivityDataRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
class JpaActivityDataRepository implements ActivityDataRepository {
    private final SpringDataActivityDataRepository data;
    JpaActivityDataRepository(SpringDataActivityDataRepository data) { this.data = data; }
    @Override public Optional<ActivityData> findById(String id) { return data.findById(id); }
    @Override public List<ActivityData> findAll(String sourceId) {
        return sourceId == null ? data.findAll() : data.findBySource_Id(sourceId);
    }
    @Override public boolean existsForSource(String sourceId) { return data.existsBySource_Id(sourceId); }
    @Override public ActivityData save(ActivityData activity) { return data.save(activity); }
    @Override public void delete(ActivityData activity) { data.delete(activity); }
}
