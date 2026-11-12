package com.horizon.ecotrack.platform.emissions.application;

import com.horizon.ecotrack.platform.emissions.domain.model.ActivityData;
import com.horizon.ecotrack.platform.emissions.domain.model.ActivityOrigin;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionRules;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import com.horizon.ecotrack.platform.emissions.domain.repository.ActivityDataRepository;
import com.horizon.ecotrack.platform.emissions.domain.repository.EmissionSourceRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityDataService {
    private final ActivityDataRepository activities;
    private final EmissionSourceRepository sources;

    public ActivityDataService(ActivityDataRepository activities, EmissionSourceRepository sources) {
        this.activities = activities;
        this.sources = sources;
    }

    @Transactional(readOnly = true)
    public List<ActivityData> list(String sourceId) {
        return activities.findAll(sourceId == null ? null : EmissionRules.text(sourceId, "emissionSourceId", 36));
    }

    @Transactional(readOnly = true)
    public ActivityData get(String id) {
        return activities.findById(id).orElseThrow(() -> new ResourceNotFoundException("Activity data not found"));
    }

    @Transactional
    public ActivityData create(String sourceId, String period, BigDecimal quantity, String unit, ActivityOrigin origin) {
        return activities.save(new ActivityData(source(sourceId), period, quantity, unit, origin));
    }

    @Transactional
    public ActivityData update(String id, String sourceId, String period, BigDecimal quantity, String unit, ActivityOrigin origin) {
        ActivityData activity = get(id);
        activity.changeDetails(source(sourceId), period, quantity, unit, origin);
        return activities.save(activity);
    }

    @Transactional
    public void delete(String id) { activities.delete(get(id)); }

    private EmissionSource source(String id) {
        String validId = EmissionRules.text(id, "emissionSourceId", 36);
        return sources.findById(validId).orElseThrow(() -> new ResourceNotFoundException("Emission source not found"));
    }
}
