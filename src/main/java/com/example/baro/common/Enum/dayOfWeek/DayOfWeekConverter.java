package com.example.baro.common.Enum.dayOfWeek;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 자동으로 모든 관련 필드에 적용
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // Enum -> DB 정수값
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return DayOfWeek.fromCode(dbData); // DB 정수값 -> Enum
    }
}
