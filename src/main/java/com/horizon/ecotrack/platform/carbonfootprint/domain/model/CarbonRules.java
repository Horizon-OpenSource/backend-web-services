package com.horizon.ecotrack.platform.carbonfootprint.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class CarbonRules {
    private CarbonRules() {}

    public static String text(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        String normalized = value.strip();
        if (normalized.length() > maxLength) throw new IllegalArgumentException(field + " is too long");
        return normalized;
    }

    public static BigDecimal positive(BigDecimal value, String field) {
        if (value == null || value.signum() <= 0) throw new IllegalArgumentException(field + " must be positive");
        BigDecimal normalized = value.stripTrailingZeros();
        long integerDigits = Math.max(0L, (long) normalized.precision() - normalized.scale());
        long fractionalDigits = Math.max(0L, (long) normalized.scale());
        if (integerDigits > 12 || fractionalDigits > 6) {
            throw new IllegalArgumentException(field + " must have at most 12 integer and 6 fractional digits");
        }
        return value;
    }

    public static <T> T required(T value, String field) {
        if (Objects.isNull(value)) throw new IllegalArgumentException(field + " is required");
        return value;
    }
}