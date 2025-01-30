package com.example.baro.common.Enum.PromisePurpose;

import com.example.baro.common.Enum.status.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 자동으로 모든 관련 필드에 적용
public class PromisePurposeConverter implements AttributeConverter<PromisePurpose, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PromisePurpose attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // Enum -> DB 정수값
    }

    @Override
    public PromisePurpose convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PromisePurpose.fromCode(dbData); // DB 정수값 -> Enum
    }
}
