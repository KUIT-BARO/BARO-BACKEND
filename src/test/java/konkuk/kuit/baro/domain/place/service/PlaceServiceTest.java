package konkuk.kuit.baro.domain.place.service;

import jakarta.persistence.EntityManager;
import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import konkuk.kuit.baro.domain.category.repository.CategoryRepository;
import konkuk.kuit.baro.domain.category.repository.PlaceCategoryRepository;
import konkuk.kuit.baro.domain.place.dto.response.PlacesResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PlaceServiceTest {

    @Autowired PlaceRepository placeRepository;
    @Autowired PlaceService placeService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired PlaceCategoryRepository placeCategoryRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired EntityManager em;

    @BeforeEach
    void init() {

        // 카테고리 생성
        Category category1 = Category.builder().categoryName("카페").build();
        Category category2 = Category.builder().categoryName("식당").build();
        Category category3 = Category.builder().categoryName("비즈니스").build();

        categoryRepository.saveAll(List.of(category1, category2, category3));


        Point point1 = geometryFactory.createPoint(new Coordinate(127.0759204, 37.5423265));
        Place place1 = Place.builder()
                .placeName("건국대학교")
                .location(point1)
                .placeAddress("대한민국 서울특별시 광진구 능동로 120")
                .build();

        Point point2 = geometryFactory.createPoint(new Coordinate(127.0782087, 37.5418772));
        Place place2 = Place.builder()
                .placeName("건국대학교 학생회관")
                .location(point2)
                .placeAddress("대한민국 서울특별시 광진구 능동로 120 건국대학교 학생회관")
                .build();

        Point point3 = geometryFactory.createPoint(new Coordinate(127.0787904, 37.541635));
        Place place3 = Place.builder()
                .placeName("건국대학교 공학관")
                .location(point3)
                .placeAddress("대한민국 서울특별시 광진구 능동로 120 건국대학교 공학관")
                .build();

        Point point4 = geometryFactory.createPoint(new Coordinate(127.038485, 37.561949));
        Place place4 = Place.builder()
                .placeName("왕십리역")
                .location(point4)
                .placeAddress("대한민국 서울특별시 행당동 왕십리")
                .build();

        placeRepository.saveAll(List.of(place1, place2, place3, place4));


        placeCategoryRepository.saveAll(List.of(
                PlaceCategory.createPlaceCategory(place1, category1),
                PlaceCategory.createPlaceCategory(place2, category1),
                PlaceCategory.createPlaceCategory(place3, category3),
                PlaceCategory.createPlaceCategory(place4, category3)
        ));

    }

    @Test
    @DisplayName("현재 좌표 기준 2KM 이내의 특정 카테고리를 포함하는 장소 조회")
    void findByDistanceAndPlaceCategory() {
        // given
        List<Long> categoryIds = List.of(1L, 2L);

        // when
        List<PlacesResponseDTO> result = placeService.placeSearch(categoryIds, 37.5423265, 127.0759204);

        // then
        assertThat(result).size().isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(doubles = {91.0000000, -91.0000000})
    @DisplayName("올바르지 않은 위도값 체크")
    void findByDistanceAndPlaceCategoryWrongLatitude(Double latitude) {

        assertThatThrownBy(() -> placeService.placeSearch(null, latitude, 127.0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LOCATION.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {181.00000, -181.0000000})
    @DisplayName("올바르지 않은 경도값 체크")
    void findByDistanceAndPlaceCategoryWrongLongitude(Double longitude) {

        assertThatThrownBy(() -> placeService.placeSearch(null, 37.2348123, longitude))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LOCATION.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 값 전달시 예외 체크")
    void findByDistanceAndPlaceCategoryWithNullAndEmptyValue(List<Long> input) {
        // when
        List<PlacesResponseDTO> result = placeService.placeSearch(input, 37.5423265, 127.0759204);

        // then
        assertThat(result).isEmpty();
    }


}