package konkuk.kuit.baro.domain.promise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidatePlace;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidateTime;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseCandidatePlaceRepositoryTest {

    @Autowired
    private PromiseRepository promiseRepository;
    @Autowired
    private PromiseMemberRepository promiseMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PromiseCandidatePlaceRepository promiseCandidatePlaceRepository;
    @Autowired
    private PromiseVoteRepository promiseVoteRepository;

    @PersistenceContext
    private EntityManager em;

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

        Promise savedPromise = promiseRepository.save(promise);

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, "#F4F4F4", user, promise);

        promiseMemberRepository.save(promiseMember);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        PromiseVote savedPromiseVote = promiseVoteRepository.save(promiseVote);

        savedPromise.setPromiseVote(savedPromiseVote);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(GeometryUtil.createPoint(122.4194155, 37.1231213))
                .placeAddress("광진구 화양동")
                .build();

        // when
        placeRepository.save(place);
    }

    @Test
    @DisplayName("약속 후보 장소 저장 테스트")
    void save() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Place place = placeRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(promiseVote, place);

        // when
        promiseCandidatePlaceRepository.save(promiseCandidatePlace);

        // then
        assertThat(promiseCandidatePlaceRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 후보 장소 삭제 테스트")
    void delete() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Place place = placeRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(promiseVote, place);

        promiseCandidatePlaceRepository.save(promiseCandidatePlace);

        em.flush();
        em.clear();

        // when
        PromiseCandidatePlace findPromiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();
        promiseCandidatePlaceRepository.delete(findPromiseCandidatePlace);

        em.flush();
        em.clear();

        // then
        assertThat(promiseCandidatePlaceRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 후보 장소 삭제 테스트")
    @Description("약속을 삭제했을 때, 약속 후보 장소도 삭제되는지 테스트. 약속 삭제 -> 약속 투표 삭제 -> 약속 후보 장소 삭제")
    void delete_promise() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Place place = placeRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(promiseVote, place);

        promiseCandidatePlaceRepository.save(promiseCandidatePlace);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseCandidatePlaceRepository.findById(1L).isEmpty()).isTrue();

    }

    @Test
    @DisplayName("약속 후보 장소 삭제 테스트")
    @Description("장소를 삭제했을 때, 약속 후보 장소도 삭제되는지 테스트. 장소 삭제 -> 약속 후보 장소 삭제")
    void delete_place() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Place place = placeRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(promiseVote, place);

        promiseCandidatePlaceRepository.save(promiseCandidatePlace);

        em.flush();
        em.clear();

        // when
        Place findPlace = placeRepository.findById(1L).get();
        placeRepository.delete(findPlace);

        em.flush();
        em.clear();

        // then
        assertThat(promiseCandidatePlaceRepository.findById(1L).isEmpty()).isTrue();

    }

}