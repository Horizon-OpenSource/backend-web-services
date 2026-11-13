package com.horizon.ecotrack.platform.analytics.domain.model;

import java.util.regex.Pattern;

/**
 * Month of the year an indicator or a report refers to, written as YYYY-MM.
 */
public record ReportingPeriod(String value) implements Comparable<ReportingPeriod> {
    private static final Pattern FORMAT = Pattern.compile("^(?!0000)\\d{4}-(0[1-9]|1[0-2])$");

    public ReportingPeriod {
        if (value == null || !FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException("period must be a valid YYYY-MM");
        }
    }

    public static ReportingPeriod of(String value) {
        return new ReportingPeriod(value);
    }

    @Override
    public int compareTo(ReportingPeriod other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
