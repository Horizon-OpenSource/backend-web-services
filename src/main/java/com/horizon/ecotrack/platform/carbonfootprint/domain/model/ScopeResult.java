package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

/** Total emissions and number of records of one scope inside a calculation. */
public record ScopeResult(FootprintScope scope, CarbonEmissions total, int records) {}
