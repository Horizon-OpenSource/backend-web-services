package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

public record ScopeResult(FootprintScope scope, CarbonEmissions total, int records) {}