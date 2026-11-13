package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.ReportingPeriod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReportingPeriodConverter implements AttributeConverter<ReportingPeriod, String> {
    @Override
    public String convertToDatabaseColumn(ReportingPeriod period) {
        return period == null ? null : period.value();
    }

    @Override
    public ReportingPeriod convertToEntityAttribute(String value) {
        return value == null ? null : new ReportingPeriod(value);
    }
}
