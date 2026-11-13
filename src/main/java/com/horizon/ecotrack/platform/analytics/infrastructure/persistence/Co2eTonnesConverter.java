package com.horizon.ecotrack.platform.analytics.infrastructure.persistence;

import com.horizon.ecotrack.platform.analytics.domain.model.Co2eTonnes;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class Co2eTonnesConverter implements AttributeConverter<Co2eTonnes, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Co2eTonnes tonnes) {
        return tonnes == null ? null : tonnes.value();
    }

    @Override
    public Co2eTonnes convertToEntityAttribute(BigDecimal value) {
        return value == null ? null : new Co2eTonnes(value);
    }
}
