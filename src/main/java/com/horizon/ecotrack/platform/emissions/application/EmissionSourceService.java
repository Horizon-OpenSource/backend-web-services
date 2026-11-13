package com.horizon.ecotrack.platform.emissions.application;

import com.horizon.ecotrack.platform.emissions.domain.model.EmissionScope;
import com.horizon.ecotrack.platform.emissions.domain.model.EmissionSource;
import com.horizon.ecotrack.platform.emissions.domain.repository.ActivityDataRepository;
import com.horizon.ecotrack.platform.emissions.domain.repository.EmissionSourceRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmissionSourceService {
    private final EmissionSourceRepository sources;
    private final ActivityDataRepository activities;

    public EmissionSourceService(EmissionSourceRepository sources, ActivityDataRepository activities) {
        this.sources = sources;
        this.activities = activities;
    }

    @Transactional(readOnly = true)
    public List<EmissionSource> list(EmissionScope scope) { return sources.findAll(scope); }

    @Transactional(readOnly = true)
    public EmissionSource get(String id) {
        return sources.findById(id).orElseThrow(() -> new ResourceNotFoundException("Emission source not found"));
    }

    @Transactional
    public EmissionSource create(String facilityId, String facilityName, String name, String type, EmissionScope scope) {
        return sources.save(new EmissionSource(facilityId, facilityName, name, type, scope));
    }

    @Transactional
    public EmissionSource update(String id, String facilityId, String facilityName, String name, String type, EmissionScope scope) {
        EmissionSource source = get(id);
        source.changeDetails(facilityId, facilityName, name, type, scope);
        return sources.save(source);
    }

    @Transactional
    public void delete(String id) {
        EmissionSource source = get(id);
        if (activities.existsForSource(id)) throw new ResourceConflictException("Delete associated activity data first");
        sources.delete(source);
    }
}
