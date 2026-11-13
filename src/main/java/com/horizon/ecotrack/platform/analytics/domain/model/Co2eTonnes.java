package com.horizon.ecotrack.platform.analytics.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Amount of emissions expressed in tonnes of CO2 equivalent. It is never negative and keeps two decimals.
 */
public record Co2eTonnes(BigDecimal value) implements Comparable<Co2eTonnes> {
    public static final Co2eTonnes ZERO = new Co2eTonnes(BigDecimal.ZERO);

    public Co2eTonnes {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException("emissions must be zero or greater");
        }
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Co2eTonnes of(double value) {
        return new Co2eTonnes(BigDecimal.valueOf(value));
    }

    public Co2eTonnes plus(Co2eTonnes other) {
        return new Co2eTonnes(value.add(other.value));
    }

    public double toDouble() {
        return value.doubleValue();
    }

    @Override
    public int compareTo(Co2eTonnes other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
