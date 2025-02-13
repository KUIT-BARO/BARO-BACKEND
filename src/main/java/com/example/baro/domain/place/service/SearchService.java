package com.example.baro.domain.place.service;

import com.example.baro.common.entity.*;
import com.example.baro.common.exception.exceptionClass.InvalidRequestException;
import com.example.baro.domain.keyword.repository.KeywordRepository;
import com.example.baro.domain.place.dto.request.ReviewPostRequestDto;
import com.example.baro.domain.place.dto.response.PlaceSearchResponseDto;
import com.example.baro.domain.place.repository.PlaceRepository;
import com.example.baro.domain.place.repository.SearchKeywordRepository;
import com.example.baro.domain.place.repository.SearchRepository;
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
    public List<PlaceSearchResponseDto> getPlacesWithFilteredKeywords(double latitude, double longitude, List<Long> keywordIds) {
        List<Object[]> results;
        // 키워드 요청이 없으면 모든 키워드를 포함하는 쿼리 실행
        if (keywordIds == null || keywordIds.isEmpty()) {
            results = placeRepository.findPlacesWithAllKeywords(latitude, longitude);
        } else {
            results = placeRepository.findPlacesWithFilteredKeywords(latitude, longitude, keywordIds);
        }

        // 결과를 placeId 기준으로 그룹핑 (한 장소에 여러 개의 키워드를 추가할 수 있도록 함)
        Map<Long, PlaceSearchResponseDto> placeMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long placeId = (Long) row[0];              // place_id
            String placeName = (String) row[4];        // place_name
            String address = (String) row[7];          // place_address
            double placeLat = (double) row[5];         // place_latitude
            double placeLon = (double) row[6];         // place_longitude

            Long keywordId = (Long) row[8];            // keyword_id
            String keywordName = (String) row[9];      // keyword_name
            int keywordCount = ((Number) row[10]).intValue(); // keyword_count

            // placeId가 없으면 빌더 생성
            placeMap.computeIfAbsent(placeId, k -> PlaceSearchResponseDto.builder()
                    .id(placeId)
                    .name(placeName)
                    .address(address)
                    .latitude(placeLat)
                    .longitude(placeLon)
                    .keywords(new ArrayList<>())
                    .build()
            );

            // 키워드 추가
            placeMap.get(placeId).keywords().add(
                    PlaceSearchResponseDto.KeywordDto.builder()
                            .id(keywordId)
                            .name(keywordName)
                            .count(keywordCount)
                            .build()
            );
        }

        // 빌더 객체에서 최종 DTO 객체 생성
        return placeMap.values().stream().toList();
    }

}
