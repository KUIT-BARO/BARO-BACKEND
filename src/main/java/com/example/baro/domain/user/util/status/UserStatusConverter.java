package com.example.baro.domain.user.util.status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 자동으로 모든 관련 필드에 적용
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // Enum -> DB 정수값
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UserStatus.fromCode(dbData); // DB 정수값 -> Enum
    }
}
