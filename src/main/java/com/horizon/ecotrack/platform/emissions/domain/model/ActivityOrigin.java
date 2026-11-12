package com.horizon.ecotrack.platform.emissions.domain.model;

/** Provenance of activity data; this does not implement CSV or API imports. */
public enum ActivityOrigin {
    MANUAL, CSV, API
}
