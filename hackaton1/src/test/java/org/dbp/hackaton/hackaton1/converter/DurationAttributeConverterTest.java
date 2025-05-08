package org.dbp.hackaton.hackaton1.converter;

import org.dbp.hackaton.hackaton1.domain.converter.DurationAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DurationAttributeConverterTest {
    private DurationAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DurationAttributeConverter();
    }

    @Test
    void convertToDatabaseColumn_WithNullDuration_ReturnsNull() {
        Long result = converter.convertToDatabaseColumn(null);
        assertNull(result, "Expected null when converting null Duration");
    }

    @Test
    void convertToDatabaseColumn_WithDuration_ReturnsSeconds() {
        Duration duration = Duration.ofHours(1).plusMinutes(30).plusSeconds(15);
        Long expected = 3600L + 1800L + 15L;
        Long result = converter.convertToDatabaseColumn(duration);
        assertEquals(expected, result, "Should convert Duration to total seconds");
    }

    @Test
    void convertToEntityAttribute_WithNullLong_ReturnsNull() {
        Duration result = converter.convertToEntityAttribute(null);
        assertNull(result, "Expected null when converting null Long");
    }

    @Test
    void convertToEntityAttribute_WithSeconds_ReturnsDuration() {
        Long seconds = 12345L;
        Duration result = converter.convertToEntityAttribute(seconds);
        assertNotNull(result, "Resulting Duration should not be null");
        assertEquals(Duration.ofSeconds(seconds), result,
                "Should convert seconds long to Duration instance");
    }
}
