package com.horizon.ecotrack.platform.emissions.domain.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/** Aggregate root representing a source at a facility. */
@Entity
@Table(name = "emission_sources")
public class EmissionSource {
    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(nullable = false, length = 128)
    private String facilityId;
    @Column(nullable = false, length = 120)
    private String facilityName;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 100)
    private String type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EmissionScope scope;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected EmissionSource() {}

    public EmissionSource(String facilityId, String facilityName, String name, String type, EmissionScope scope) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        changeDetails(facilityId, facilityName, name, type, scope);
    }

    public void changeDetails(String facilityId, String facilityName, String name, String type, EmissionScope scope) {
        String validFacilityId = EmissionRules.text(facilityId, "facilityId", 128);
        String validFacilityName = EmissionRules.text(facilityName, "facilityName", 120);
        String validName = EmissionRules.text(name, "name", 120);
        String validType = EmissionRules.text(type, "type", 100);
        EmissionScope validScope = EmissionRules.required(scope, "scope");
        this.facilityId = validFacilityId;
        this.facilityName = validFacilityName;
        this.name = validName;
        this.type = validType;
        this.scope = validScope;
    }

    public String getId() { return id; }
    public String getFacilityId() { return facilityId; }
    public String getFacilityName() { return facilityName; }
    public String getName() { return name; }
    public String getType() { return type; }
    public EmissionScope getScope() { return scope; }
    public Instant getCreatedAt() { return createdAt; }
}
