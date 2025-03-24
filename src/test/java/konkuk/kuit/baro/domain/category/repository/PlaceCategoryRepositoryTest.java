package konkuk.kuit.baro.domain.category.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceCategoryRepositoryTest {

    @Autowired private PlaceCategoryRepository placeCategoryRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PlaceRepository placeRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @PersistenceContext private EntityManager em;

    @BeforeEach
    void init() {
        Category category = Category.builder()
                .categoryName("비즈니스")
                .build();

        categoryRepository.save(category);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(geometryFactory.createPoint(new Coordinate(37.7749295, -122.4194155)))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);
    }

    @Test
    @DisplayName("장소 카테고리 저장 테스트")
    void save() {
        // given
        Place findPlace = placeRepository.findById(1L).get();
        Category findCategory = categoryRepository.findById(1L).get();

        // when
        PlaceCategory placeCategory = PlaceCategory.createPlaceCategory(findPlace, findCategory);

        placeCategoryRepository.save(placeCategory);

        assertThat(placeCategoryRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("장소 카테고리 삭제 테스트")
    void delete() {
        // given
        Place findPlace = placeRepository.findById(1L).get();
        Category findCategory = categoryRepository.findById(1L).get();
        PlaceCategory placeCategory = PlaceCategory.createPlaceCategory(findPlace, findCategory);

        placeCategoryRepository.save(placeCategory);

        em.flush();
        em.clear();

        // when
        PlaceCategory findPlaceCategory = placeCategoryRepository.findById(1L).get();
        placeCategoryRepository.delete(findPlaceCategory);

        em.flush();
        em.clear();

        // then
        assertThat(placeCategoryRepository.findById(1L).isEmpty()).isTrue();


    }

}