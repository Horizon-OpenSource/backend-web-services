package com.horizon.ecotrack.platform.emissions.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

/** Invariants shared by every entry point into the emissions domain. */
public final class EmissionRules {
    private static final Pattern PERIOD = Pattern.compile("^(?!0000)\\d{4}-(0[1-9]|1[0-2])$");

    private EmissionRules() {}

    public static String text(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        String normalized = value.strip();
        if (normalized.length() > maxLength) throw new IllegalArgumentException(field + " is too long");
        return normalized;
    }

    public static String period(String value) {
        if (value == null || !PERIOD.matcher(value).matches()) {
            throw new IllegalArgumentException("period must be a valid YYYY-MM");
        }
        return value;
    }

    public static BigDecimal quantity(BigDecimal value) {
        if (value == null || value.signum() <= 0) throw new IllegalArgumentException("quantity must be positive");
        BigDecimal normalized = value.stripTrailingZeros();
        long integerDigits = Math.max(0L, (long) normalized.precision() - normalized.scale());
        long fractionalDigits = Math.max(0L, (long) normalized.scale());
        if (integerDigits > 12 || fractionalDigits > 6) {
            throw new IllegalArgumentException("quantity must have at most 12 integer and 6 fractional digits");
        }
        return value;
    }

    public static <T> T required(T value, String field) {
        if (Objects.isNull(value)) throw new IllegalArgumentException(field + " is required");
        return value;
    }
}
