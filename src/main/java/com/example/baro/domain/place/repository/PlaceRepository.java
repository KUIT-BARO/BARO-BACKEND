package com.example.baro.domain.place.repository;

import com.example.baro.common.entity.Place;
import com.example.baro.domain.place.repository.projection.PlaceSearchProjection;
import org.springframework.data.domain.Pageable;
import com.example.baro.common.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query(value = "SELECT p FROM Place p ORDER BY FUNCTION('RAND')", nativeQuery = true)
    List<Place> findRandomPlaces(Pageable pageable);

    Optional<Place> findByAddress(String address);

    // 반경 2km내 place들 조회
    // Haversine 공식 사용

    // 키워드 요청이 없는 경우
    @Query(value = """
        WITH NearbyPlaces AS (
            SELECT p.*
            FROM place p
            WHERE p.latitude BETWEEN (:latitude - 0.018) AND (:latitude + 0.018)
            AND p.longitude BETWEEN (:longitude - 0.022) AND (:longitude + 0.022)
            AND 6371 * acos(
                cos(radians(:latitude)) * cos(radians(p.latitude))
                * cos(radians(p.longitude) - radians(:longitude))
                + sin(radians(:latitude)) * sin(radians(p.latitude))
            ) <= 2
        ),
        KeywordCounts AS (
            SELECT sk.search_id, sk.keyword_id, COUNT(*) AS count
            FROM search_keyword sk
            JOIN search s ON sk.search_id = s.id
            WHERE s.place_id IN (SELECT id FROM NearbyPlaces)
            GROUP BY sk.search_id, sk.keyword_id
        ),
        MedianValues AS (
            SELECT s.place_id, PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY count) AS median
            FROM KeywordCounts kc
            JOIN search s ON kc.search_id = s.id
            GROUP BY s.place_id
        )
        SELECT p.*, k.id AS keyword_id, k.name AS keyword_name, kc.count
        FROM NearbyPlaces p
        JOIN search s ON p.id = s.place_id
        JOIN KeywordCounts kc ON kc.search_id = s.id
        JOIN keyword k ON kc.keyword_id = k.id
        JOIN MedianValues mv ON s.place_id = mv.place_id
        WHERE kc.count >= mv.median
        ORDER BY p.id, kc.count DESC;
        """, nativeQuery = true)
    List<PlaceSearchProjection> findPlacesWithAllKeywords(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude
    );

    @Query(value = """
        WITH NearbyPlaces AS (
            SELECT p.*
            FROM place p
            WHERE p.latitude BETWEEN (:latitude - 0.018) AND (:latitude + 0.018)
            AND p.longitude BETWEEN (:longitude - 0.022) AND (:longitude + 0.022)
            AND 6371 * acos(
                cos(radians(:latitude)) * cos(radians(p.latitude))
                * cos(radians(p.longitude) - radians(:longitude))
                + sin(radians(:latitude)) * sin(radians(p.latitude))
            ) <= 2
        ),
        KeywordCounts AS (
            SELECT sk.search_id, sk.keyword_id, COUNT(*) AS count
            FROM search_keyword sk
            JOIN search s ON sk.search_id = s.id
            WHERE s.place_id IN (SELECT id FROM NearbyPlaces)
            GROUP BY sk.search_id, sk.keyword_id
        ),
        MedianValues AS (
            SELECT s.place_id, PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY count) AS median
            FROM KeywordCounts kc
            JOIN search s ON kc.search_id = s.id
            GROUP BY s.place_id
        ),
        FilteredKeywords AS (
            SELECT kc.search_id, kc.keyword_id, kc.count
            FROM KeywordCounts kc
            JOIN MedianValues mv ON kc.search_id = mv.place_id
            WHERE kc.count >= mv.median
            AND kc.keyword_id IN (:keywordIds) -- 클라이언트가 전달한 키워드 중 하나 이상 포함
        )
        SELECT p.*, k.id AS keyword_id, k.name AS keyword_name, fk.count
        FROM NearbyPlaces p
        JOIN search s ON p.id = s.place_id
        JOIN FilteredKeywords fk ON fk.search_id = s.id
        JOIN keyword k ON fk.keyword_id = k.id
        ORDER BY p.id, fk.count DESC
        """, nativeQuery = true)
    List<PlaceSearchProjection> findPlacesWithFilteredKeywords(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("keywordIds") List<Long> keywordIds
    );
    /*
    WITH NearbyPlaces AS (...) → 반경 2km 내 place 리스트 조회
    WITH KeywordCounts AS (...) → 각 장소의 키워드 등장 횟수 집계
    WITH MedianValues AS (...) → 각 장소별 키워드 중간값 계산
    WHERE kc.count >= mv.median → 중간값 이상인 키워드만 포함
     */
}
