package konkuk.kuit.baro.domain.place.service;

import jakarta.persistence.EntityManager;
import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import konkuk.kuit.baro.domain.category.repository.CategoryRepository;
import konkuk.kuit.baro.domain.category.repository.PlaceCategoryRepository;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.pin.repository.PinRepository;
import konkuk.kuit.baro.domain.place.dto.request.PlaceSearchRequestDTO;
import konkuk.kuit.baro.domain.place.dto.response.PinListResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSearchResponseDTO;
import konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PlaceServiceTest {

    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    PlaceService placeService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PlaceCategoryRepository placeCategoryRepository;
    @Autowired
    UserRepository userRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired
    EntityManager em;
    @Autowired
    private PinRepository pinRepository;

    @BeforeEach
    void init() {
        // 1. 카테고리 생성 (총 5개)
        Category category1 = Category.builder().categoryName("카페").build();
        Category category2 = Category.builder().categoryName("식당").build();
        Category category3 = Category.builder().categoryName("비즈니스").build();
        Category category4 = Category.builder().categoryName("교육").build();
        Category category5 = Category.builder().categoryName("교통").build();
        categoryRepository.saveAll(List.of(category1, category2, category3, category4, category5));

        // 2. 사용자 생성 (총 3명)
        User user1 = User.builder()
                .email("hong@konkuk.ac.kr")
                .name("홍길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .build();

        User user2 = User.builder()
                .email("kim@konkuk.ac.kr")
                .name("김길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .build();

        User user3 = User.builder()
                .email("park@konkuk.ac.kr")
                .name("박길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));

        // 3. 장소 생성 (총 6개)
        // 장소1: 건국대학교 - 핀 3개, 카테고리 2개
        Place place1 = Place.builder()
                .placeName("건국대학교")
                .location(geometryFactory.createPoint(new Coordinate(127.0759204, 37.5423265)))
                .placeAddress("서울 광진구 능동로 120")
                .build();

        // 장소2: 학생회관 - 핀 1개, 카테고리 1개
        Place place2 = Place.builder()
                .placeName("학생회관")
                .location(geometryFactory.createPoint(new Coordinate(127.0782087, 37.5418772)))
                .placeAddress("서울 광진구 능동로 120-1")
                .build();

        // 장소3: 공학관 - 핀 0개, 카테고리 1개
        Place place3 = Place.builder()
                .placeName("공학관")
                .location(geometryFactory.createPoint(new Coordinate(127.0787904, 37.541635)))
                .placeAddress("서울 광진구 능동로 120-2")
                .build();

        // 장소4: 왕십리역 - 핀 4개, 카테고리 1개
        Place place4 = Place.builder()
                .placeName("왕십리역")
                .location(geometryFactory.createPoint(new Coordinate(127.038485, 37.561949)))
                .placeAddress("서울 성동구 왕십리로")
                .build();

        // 장소5: 테스트장소1 - 핀 0개, 카테고리 0개
        Place place5 = Place.builder()
                .placeName("테스트장소1")
                .location(geometryFactory.createPoint(new Coordinate(127.0, 37.0)))
                .placeAddress("테스트 주소1")
                .build();

        // 장소6: 테스트장소2 - 핀 2개, 카테고리 3개
        Place place6 = Place.builder()
                .placeName("테스트장소2")
                .location(geometryFactory.createPoint(new Coordinate(127.1, 37.1)))
                .placeAddress("테스트 주소2")
                .build();

        placeRepository.saveAll(List.of(place1, place2, place3, place4, place5, place6));

        // 4. 장소-카테고리 연결
        placeCategoryRepository.saveAll(List.of(
                // 건국대: 카페 + 교육
                PlaceCategory.createPlaceCategory(place1, category1),
                PlaceCategory.createPlaceCategory(place1, category4),
                // 학생회관: 식당
                PlaceCategory.createPlaceCategory(place2, category2),
                // 공학관: 비즈니스
                PlaceCategory.createPlaceCategory(place3, category3),
                // 왕십리역: 교통
                PlaceCategory.createPlaceCategory(place4, category5),
                // 테스트장소2: 카페 + 식당 + 교육
                PlaceCategory.createPlaceCategory(place6, category1),
                PlaceCategory.createPlaceCategory(place6, category2),
                PlaceCategory.createPlaceCategory(place6, category4)
        ));

        // 5. 핀 데이터 생성
        List<Pin> pins = List.of(
                // 건국대 (평균 별점 = (5+4+3)/3 = 4.0)
                Pin.createPin("건국대 리뷰1", (short) 5, user1, place1),
                Pin.createPin("건국대 리뷰2", (short) 4, user2, place1),
                Pin.createPin("건국대 리뷰3", (short) 3, user3, place1),

                // 학생회관 (평균 4.0)
                Pin.createPin("학생회관 리뷰", (short) 4, user1, place2),

                // 왕십리역 (평균 = (5+5+4+2)/4 = 4.0)
                Pin.createPin("왕십리역 리뷰1", (short) 5, user1, place4),
                Pin.createPin("왕십리역 리뷰2", (short) 5, user2, place4),
                Pin.createPin("왕십리역 리뷰3", (short) 4, user3, place4),
                Pin.createPin("왕십리역 리뷰4", (short) 2, user1, place4),

                // 테스트장소2 (평균 = (1+5)/2 = 3.0)
                Pin.createPin("테스트 리뷰1", (short) 1, user1, place6),
                Pin.createPin("테스트 리뷰2", (short) 5, user2, place6)
        );
        pinRepository.saveAll(pins);
    }

    @Test
    @DisplayName("현재 좌표 기준 2KM 이내의 특정 카테고리를 포함하는 장소 조회")
    void findByDistanceAndPlaceCategory() {
        // given
        List<Long> categoryIds = List.of(1L, 2L);

        // when
        List<PlaceSearchResponseDTO> result = placeService.placeSearch(new PlaceSearchRequestDTO(categoryIds, 37.5423265, 127.0759204));

        // then
        assertThat(result).size().isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(doubles = {91.0000000, -91.0000000})
    @DisplayName("올바르지 않은 위도값 체크")
    void findByDistanceAndPlaceCategoryWrongLatitude(Double latitude) {

        assertThatThrownBy(() -> placeService.placeSearch(new PlaceSearchRequestDTO(List.of(1L, 2L), latitude, 127.0)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LOCATION.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {181.00000, -181.0000000})
    @DisplayName("올바르지 않은 경도값 체크")
    void findByDistanceAndPlaceCategoryWrongLongitude(Double longitude) {

        assertThatThrownBy(() -> placeService.placeSearch(new PlaceSearchRequestDTO(List.of(1L, 2L), 37.2348123, longitude)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_LOCATION.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null or [] 값 전달시 체크")
    void findByDistanceAndPlaceCategoryWithNullAndEmptyValue(List<Long> input) {
        // when
        List<PlaceSearchResponseDTO> result = placeService.placeSearch(new PlaceSearchRequestDTO(input, 37.5423265, 127.0759204));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("placeId를 통해 장소 요약 정보 조회 테스트")
    void findPlaceSummaryById() {
        PlaceSummaryInfoResponseDTO result = placeService.placeSummaryInfo(1L);

        assertThat(result.getPlaceCategories())
                .containsExactly("카페", "교육");
    }

    @Test
    @DisplayName("핀과 카테고리가 모두 없는 장소 조회 테스트")
    void findPlaceWithNoPinsAndCategories() {
        PlaceSummaryInfoResponseDTO result = placeService.placeSummaryInfo(5L);

        assertThat(result.getStar()).isNull();
        assertThat(result.getPinCount()).isZero();
        assertThat(result.getPlaceCategories()).isEmpty();
    }

    @Test
    @DisplayName("특정 장소에 대한 핀 목록 조회 테스트")
    void findPinListByPlaceId() {
        List<PinListResponseDTO> result = placeService.placePinList(1L);

        assertThat(result).size().isEqualTo(3);
    }

    @Test
    @DisplayName("핀이 등록되어있지 않은 장소에 대한 핀 목록 조회")
    void findPinListWithNoPins() {
        List<PinListResponseDTO> result = placeService.placePinList(5L);

        assertThat(result).isEmpty();

    }


}