package konkuk.kuit.baro.domain.place.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("장소 저장 테스트")
    void save() {
        // given
        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .longitude(new BigDecimal("37.7749295"))
                .latitude(new BigDecimal("-122.4194155"))
                .placeAddress("광진구 화양동")
                .build();

        // when
        placeRepository.save(place);

        em.flush();
        em.clear();

        // then
        assertThat(placeRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("장소 삭제 테스트")
    void delete() {
        // given
        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .longitude(new BigDecimal("37.7749295"))
                .latitude(new BigDecimal("-122.4194155"))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);

        em.flush();
        em.clear();

        // when
        Place findPlace = placeRepository.findById(1L).get();
        placeRepository.delete(findPlace);

        em.flush();
        em.clear();

        // then
        assertThat(placeRepository.findById(1L).isEmpty()).isTrue();

    }

}