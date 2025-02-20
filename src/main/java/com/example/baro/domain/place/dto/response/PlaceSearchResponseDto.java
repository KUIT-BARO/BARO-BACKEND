package com.example.baro.domain.place.dto.response;

import com.example.baro.common.Enum.userProfile.UserProfile;
import com.example.baro.domain.place.service.SearchService;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PlaceSearchResponseDto(
        List<PlaceDto> places
) {
    @Builder
    public record PlaceDto(
            Long id,
            String name,
            String address,
            Double latitude,
            Double longitude,
            ArrayList<KeywordDto> keywords
    ) {}


    @Builder
    public record KeywordDto(
            Long id,
            String name,
            Integer count
    ) { }
}