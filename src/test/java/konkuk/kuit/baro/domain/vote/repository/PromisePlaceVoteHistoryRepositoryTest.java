package konkuk.kuit.baro.domain.vote.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidatePlace;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.model.PromiseSuggestedPlace;
import konkuk.kuit.baro.domain.promise.repository.PromiseCandidatePlaceRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseSuggestedPlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromisePlaceVoteHistoryRepositoryTest {

    @Autowired
    private PromisePlaceVoteHistoryRepository promisePlaceVoteHistoryRepository;
    @Autowired
    private PromiseRepository promiseRepository;
    @Autowired
    private PromiseMemberRepository promiseMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PromiseVoteRepository promiseVoteRepository;

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PromiseCandidatePlaceRepository promiseCandidatePlaceRepository;

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

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, "#F4F4F4", user, promise);

        promiseMemberRepository.save(promiseMember);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(geometryFactory.createPoint(new Coordinate(37.7749295, -122.4194155)))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);

        PromiseSuggestedPlace promiseSuggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(promiseMember, place);
        promiseSuggestedPlaceRepository.save(promiseSuggestedPlace);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        promiseVoteRepository.save(promiseVote);

        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(promiseVote, place);

        promiseCandidatePlaceRepository.save(promiseCandidatePlace);
    }

    @Test
    @DisplayName("약속 장소 투표 내역 저장 테스트")
    void save() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        PromiseMember promiseMember = promiseMemberRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();

        // when
        PromisePlaceVoteHistory promisePlaceVoteHistory = PromisePlaceVoteHistory.createPromisePlaceVoteHistory(promiseCandidatePlace, promiseVote, promiseMember);

        promisePlaceVoteHistoryRepository.save(promisePlaceVoteHistory);

        em.flush();
        em.clear();

        // then
        assertThat(promiseVoteRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 장소 투표 내역 삭제 테스트")
    void delete() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        PromiseMember promiseMember = promiseMemberRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();

        PromisePlaceVoteHistory promisePlaceVoteHistory = PromisePlaceVoteHistory.createPromisePlaceVoteHistory(promiseCandidatePlace, promiseVote, promiseMember);

        promisePlaceVoteHistoryRepository.save(promisePlaceVoteHistory);

        em.flush();
        em.clear();

        // when
        PromisePlaceVoteHistory findPromisePlaceVoteHistory = promisePlaceVoteHistoryRepository.findById(1L).get();
        promisePlaceVoteHistoryRepository.delete(findPromisePlaceVoteHistory);

        em.flush();
        em.clear();

        // then
        assertThat(promisePlaceVoteHistoryRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 장소 투표 내역 삭제 테스트")
    @Description("약속을 삭제했을 때, 약속 장소 투표 내역도 삭제되는 지 테스트. 약속 삭제 -> 약속 투표 삭제 -> 약속 후보 장소 삭제 -> 약속 장소 투표 내역 삭제")
    void delete_promise() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Promise promise = promiseRepository.findById(1L).get();
        PromiseMember promiseMember = promiseMemberRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();

        promise.setPromiseVote(promiseVote);

        PromisePlaceVoteHistory promisePlaceVoteHistory = PromisePlaceVoteHistory.createPromisePlaceVoteHistory(promiseCandidatePlace, promiseVote, promiseMember);

        promisePlaceVoteHistoryRepository.save(promisePlaceVoteHistory);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promisePlaceVoteHistoryRepository.findById(1L).isEmpty()).isTrue();

    }

    @Test
    @DisplayName("유저 삭제 시 약속 장소 투표 내역 삭제 테스트")
    @Description("유저를 삭제했을 때, 약속 장소 투표 내역도 삭제되는 지 테스트. 유저 삭제 -> 약속 참여자 삭제 -> 약속 장소 투표 내역 삭제")
    void delete_User() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Promise promise = promiseRepository.findById(1L).get();
        PromiseMember promiseMember = promiseMemberRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();

        promise.setPromiseVote(promiseVote);

        PromisePlaceVoteHistory promisePlaceVoteHistory = PromisePlaceVoteHistory.createPromisePlaceVoteHistory(promiseCandidatePlace, promiseVote, promiseMember);

        promisePlaceVoteHistoryRepository.save(promisePlaceVoteHistory);

        em.flush();
        em.clear();

        // when
        User findUser = userRepository.findById(1L).get();
        userRepository.delete(findUser);

        em.flush();
        em.clear();

        // then
        assertThat(promisePlaceVoteHistoryRepository.findById(1L).isEmpty()).isTrue();
    }



}