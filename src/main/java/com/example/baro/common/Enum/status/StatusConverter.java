package com.example.baro.common.Enum.status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 자동으로 모든 관련 필드에 적용
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // Enum -> DB 정수값
    }

    @Override
    public Status convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Status.fromCode(dbData); // DB 정수값 -> Enum
    }
}
