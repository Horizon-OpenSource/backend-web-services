package com.horizon.ecotrack.platform.emissions.infrastructure.persistence;

import com.horizon.ecotrack.platform.emissions.domain.model.ActivityData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataActivityDataRepository extends JpaRepository<ActivityData, String> {
    List<ActivityData> findBySource_Id(String sourceId);
    boolean existsBySource_Id(String sourceId);
}
