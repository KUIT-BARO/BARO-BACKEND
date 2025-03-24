package konkuk.kuit.baro.domain.place.service;

import jakarta.persistence.EntityManager;
import konkuk.kuit.baro.domain.place.dto.response.PlacesResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class PlaceServiceTest {

    @Autowired PlaceRepository placeRepository;
    @Autowired PlaceService placeService;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Autowired EntityManager em;

    @BeforeEach
    void init() {

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

        placeRepository.save(place1);
        placeRepository.save(place2);
        placeRepository.save(place3);
        placeRepository.save(place4);
    }

    @Test
    @DisplayName("현재 좌표 기준 2KM 이내의 장소 조회")
    void findByDistance() {
        Assertions.assertThat(placeService.placeSearch(37.5423265, 127.0759204)).size().isEqualTo(3);
    }


}