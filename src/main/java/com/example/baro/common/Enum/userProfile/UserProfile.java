package com.example.baro.common.Enum.userProfile;

import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserProfile {
    MAN(1),
    WOMAN(2),
    DOG(3),
    NONE(4);

    private final int code;

    UserProfile(int code) {
        this.code = code;
    }

    public static UserProfile fromCode(int code) {
        for (UserProfile profile : UserProfile.values()) {
            if (profile.getCode() == code) {
                return profile;
            }
        }
        throw new IllegalArgumentException("알 수 없는 code입니다." + code);
    }



    public static UserProfile fromString(String profileImage) {
        return Arrays.stream(UserProfile.values())
                .filter(s -> s.name().equalsIgnoreCase(profileImage))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("잘못된 프로필 이미지입니다. :  " + profileImage));
    }


}