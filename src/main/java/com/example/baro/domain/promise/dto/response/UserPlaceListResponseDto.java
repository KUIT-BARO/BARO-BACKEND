package com.example.baro.domain.promise.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserPlaceListResponseDto (
        List<UserPlaceDto> userPlaceDto
){


    @Builder
    public record UserPlaceDto(
            Long placeId,
            String address,
            String placeName
    ){}
}
