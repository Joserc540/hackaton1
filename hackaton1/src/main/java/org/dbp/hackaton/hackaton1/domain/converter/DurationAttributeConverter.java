package org.dbp.hackaton.hackaton1.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationAttributeConverter implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return (attribute == null ? null : attribute.getSeconds());
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return (dbData == null ? null : Duration.ofSeconds(dbData));
    }
}
