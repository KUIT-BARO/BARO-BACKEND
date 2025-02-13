package com.example.baro.domain.place.controller;


import com.example.baro.common.dto.ApiResponseDto;
import com.example.baro.common.dto.enums.SuccessCode;
import com.example.baro.common.entity.User;
import com.example.baro.common.resolver.LoginUser;
import com.example.baro.domain.place.dto.request.ReviewPostRequestDto;
import com.example.baro.domain.place.dto.response.PlaceSearchResponseDto;
import com.example.baro.domain.place.service.PlaceService;
import com.example.baro.domain.place.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final SearchService searchService;

    @PostMapping("/{placeId}/reviews")
    public ApiResponseDto postReview(@Valid @RequestBody ReviewPostRequestDto requestDto, @LoginUser User user, @PathVariable Long placeId) {
        searchService.postReview(requestDto,user, placeId);
        return ApiResponseDto.success(SuccessCode.NOTE_CREATE_SUCCESS);
    }

    // 사용하는 곳이 없음
    @GetMapping("/search")
    public List<PlaceSearchResponseDto> getNearbyPlacesWithFilteredKeywords(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) List<Long> keywordIds) {
        return searchService.getPlacesWithFilteredKeywords(latitude, longitude, keywordIds);
    }


}
