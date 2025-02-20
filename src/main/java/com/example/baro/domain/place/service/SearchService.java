package com.example.baro.domain.place.service;

import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import com.example.baro.domain.keyword.repository.KeywordRepository;
import com.example.baro.domain.place.dto.request.ReviewPostRequestDto;
import com.example.baro.domain.place.dto.response.PlaceSearchResponseDto;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.place.repository.SearchKeywordRepository;
import com.example.baro.domain.place.repository.SearchRepository;
import com.example.baro.domain.place.repository.projection.PlaceSearchProjection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;
    private final SearchKeywordRepository searchKeywordRepository;
    private final PlaceRepository placeRepository;
    private final KeywordRepository keywordRepository;

    public void postReview(@Valid ReviewPostRequestDto requestDto, User user, Long placeId) {
        Place place = placeRepository
                .findById(placeId)
                .orElseThrow(() -> new InvalidRequestException("존재 하지 않는 place입니다."));


        SearchKeyword searchKeyword = SearchKeyword.builder().build();

        Search search = Search.builder()
                .user(user)
                .place(place)
                .note(requestDto.note())
                .build();
        searchRepository.save(search);


        // 3. `keywordIds`를 `SearchKeyword`로 변환하여 저장
        List<SearchKeyword> searchKeywords = requestDto.keywordIds().stream()
                .map(keywordId -> {
                    Keyword keyword = keywordRepository.findById(keywordId)
                            .orElseThrow(() -> new InvalidRequestException("존재하지 않는 keywordId: " + keywordId));
                    return SearchKeyword.builder()
                            .search(search)
                            .keyword(keyword)
                            .build();
                })
                .toList();

        searchKeywordRepository.saveAll(searchKeywords);
    }


    @Transactional(readOnly = true)
    public PlaceSearchResponseDto getPlacesWithFilteredKeywords(double latitude, double longitude, List<Long> keywordIds) {

        // 키워드 필터링 여부에 따라 적절한 Repository 메서드 호출
        List<PlaceSearchProjection> results = (keywordIds == null || keywordIds.isEmpty())
                ? placeRepository.findPlacesWithAllKeywords(latitude, longitude)
                : placeRepository.findPlacesWithFilteredKeywords(latitude, longitude, keywordIds);

        // placeId 기준으로 그룹화
        Map<Long, PlaceSearchResponseDto.PlaceDto> placeMap = new LinkedHashMap<>();

        for (PlaceSearchProjection result : results) {
            // placeId가 없는 경우, 새로운 DTO 객체 생성하여 Map에 저장
            placeMap.computeIfAbsent(result.getPlaceId(), id ->
                    PlaceSearchResponseDto.PlaceDto.builder()
                            .id(result.getPlaceId())
                            .name(result.getPlaceName())
                            .address(result.getPlaceAddress())
                            .latitude(result.getPlaceLatitude())
                            .longitude(result.getPlaceLongitude())
                            .keywords(new ArrayList<>()) //  ArrayList 초기화
                            .build()
            );

            // 기존 객체의 키워드 리스트 가져와서 추가
            placeMap.get(result.getPlaceId()).keywords().add(
                    PlaceSearchResponseDto.KeywordDto.builder()
                            .id(result.getKeywordId())
                            .name(result.getKeywordName())
                            .count(result.getKeywordCount())
                            .build()
            );
        }

        // 최종 변환된 DTO 리스트 반환
        return PlaceSearchResponseDto.builder()
                .places(new ArrayList<>(placeMap.values())) // `Collection`을 `List`로 변환하여 전달
                .build();
    }

}
