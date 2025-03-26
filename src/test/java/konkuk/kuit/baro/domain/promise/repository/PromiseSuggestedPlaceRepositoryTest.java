package konkuk.kuit.baro.domain.promise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseSuggestedPlaceRepositoryTest {

    @Autowired private PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private PromiseMemberRepository promiseMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PromiseRepository promiseRepository;

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

        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        promiseRepository.save(promise);

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true,"#F4F4F4", user, promise);

        promiseMemberRepository.save(promiseMember);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(geometryFactory.createPoint(new Coordinate(37.7749295, -122.4194155)))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);
    }

    @Test
    @DisplayName("제안된 약속 장소 저장 테스트")
    void save() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();

        // when
        PromiseSuggestedPlace promiseSuggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(findPromiseMember, findPlace);
        promiseSuggestedPlaceRepository.save(promiseSuggestedPlace);

        em.flush();
        em.clear();

        // then
        assertThat(promiseSuggestedPlaceRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("제안된 약속 장소 삭제 테스트")
    @Description("약속이 삭제되었을 때, 제안된 약속 장소도 삭제되는 지 테스트. 약속 삭제 -> 약속 참여자 삭제 -> 제안된 약속 장소 삭제")
    void delete_promise() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();

        PromiseSuggestedPlace promiseSuggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(findPromiseMember, findPlace);
        promiseSuggestedPlaceRepository.save(promiseSuggestedPlace);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
        assertThat(promiseSuggestedPlaceRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("제안된 약속 장소 삭제 테스트")
    @Description("유저가 삭제되었을 때, 제안된 약속 장소도 삭제되는 지 테스트. 유저 삭제 -> 약속 참여자 삭제 -> 제안된 약속 장소 삭제")
    void delete_User() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();

        PromiseSuggestedPlace promiseSuggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(findPromiseMember, findPlace);
        promiseSuggestedPlaceRepository.save(promiseSuggestedPlace);

        em.flush();
        em.clear();

        // when
        User findUser = userRepository.findById(1L).get();
        userRepository.delete(findUser);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
        assertThat(promiseSuggestedPlaceRepository.findById(1L).isEmpty()).isTrue();
    }


}