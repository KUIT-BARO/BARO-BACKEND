package konkuk.kuit.baro.domain.pin.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
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
class PinRepositoryTest {

    @Autowired private PinRepository pinRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PlaceRepository placeRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @PersistenceContext private EntityManager em;

    @BeforeEach
    void init() {
        User user = User.builder()
                .email("hong@konkuk.ac.kr")
                .name("홍길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .build();

        userRepository.save(user);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(geometryFactory.createPoint(new Coordinate(37.7749295, -122.4194155)))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);
    }

    @Test
    @DisplayName("핀 저장 테스트")
    void save() {
        // given
        User findUser = userRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();

        // when
        Pin pin = Pin.createPin("아늑해요", (short) 5, findUser, findPlace);
        pinRepository.save(pin);

        em.flush();
        em.clear();

        // then
        assertThat(pinRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("핀 삭제 테스트")
    void delete() {
        // given
        User findUser = userRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();
        Pin pin = Pin.createPin("아늑해요", (short) 5, findUser, findPlace);
        pinRepository.save(pin);

        em.flush();
        em.clear();

        // when
        Pin findPin = pinRepository.findById(1L).get();
        pinRepository.delete(findPin);

        em.flush();
        em.clear();

        // then
        assertThat(pinRepository.findById(1L).isEmpty()).isTrue();

    }

}