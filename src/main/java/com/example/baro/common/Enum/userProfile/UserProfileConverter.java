package com.example.baro.common.Enum.userProfile;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 자동으로 모든 관련 필드에 적용
public class UserProfileConverter implements AttributeConverter<UserProfile, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserProfile attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode(); // Enum -> DB 정수값
    }

    @Override
    public UserProfile convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return UserProfile.fromCode(dbData); // DB 정수값 -> Enum
    }
}
