package konkuk.kuit.baro.domain.promise.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.dto.response.ConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.dto.response.PromiseStatusConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.model.*;
import konkuk.kuit.baro.domain.promise.repository.*;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromisePlaceVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromisePlaceVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseTimeVoteHistoryRepository;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
import konkuk.kuit.baro.global.common.util.DateUtil;
import konkuk.kuit.baro.global.common.util.GeometryUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseServiceTest {

    @Autowired
    PromiseMemberRepository promiseMemberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PromiseRepository promiseRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    PromiseSuggestedPlaceRepository promiseSuggestedPlaceRepository;
    @Autowired
    PromiseCandidatePlaceRepository promiseCandidatePlaceRepository;
    @Autowired
    PromiseCandidateTimeRepository promiseCandidateTimeRepository;
    @Autowired
    PromiseTimeVoteHistoryRepository promiseTimeVoteHistoryRepository;
    @Autowired
    PromisePlaceVoteHistoryRepository promisePlaceVoteHistoryRepository;

    @Autowired
    private PromiseService promiseService;
    @Autowired
    private PromiseVoteRepository promiseVoteRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("약속 현황 - 확정 테스트")
    void confirm() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        promiseRepository.save(promise);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(GeometryUtil.createPoint(37.7749295, 122.4194155))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);

        em.flush();
        em.clear();

        // when

        Promise findPromise = promiseRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();

        findPromise.setPlace(findPlace);
        findPromise.setFixedDate(LocalDate.now());
        findPromise.setFixedTime(LocalTime.now());

        em.flush();
        em.clear();

        // then
        PromiseStatusConfirmedPromiseResponseDTO confirmedPromise = promiseService.getConfirmedPromise(findPromise.getId());

        assertThat(confirmedPromise.getPromiseName()).isEqualTo("컴퓨터 공학부 개강파티");
        assertThat(confirmedPromise.getConfirmedPlace()).isEqualTo("스타벅스 건대점");
        assertThat(confirmedPromise.getLatitude()).isEqualTo(37.7749295);
        assertThat(confirmedPromise.getLongitude()).isEqualTo(122.4194155);
    }

    @Test
    @DisplayName("약속 확정 에러 테스트")
    void confirm_exception() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        Promise savedPromise = promiseRepository.save(promise);

        em.flush();
        em.clear();

        // then
        assertThatThrownBy(() -> promiseService.getConfirmedPromise(savedPromise.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PROMISE_NOT_CONFIRMED.getMessage());
    }

    @Test
    @DisplayName("약속 제안 남은 시간 조회 테스트")
    void promiseSuggestRemainingTime() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        Promise savedPromise = promiseRepository.save(promise);

        em.flush();
        em.clear();


        assertThat(promiseService.getPromiseSuggestRemainingTime(savedPromise.getId()).getPromiseSuggestRemainingTime()).isEqualTo("D-1");
    }

    @Test
    @DisplayName("약속 제안 남은 시간 조회 예외 테스트")
    void promiseSuggestRemainingTimeException() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().minusDays(2))
                .build();

        Promise savedPromise = promiseRepository.save(promise);

        em.flush();
        em.clear();

        // when & then
        assertThatThrownBy(() -> promiseService.getPromiseSuggestRemainingTime(savedPromise.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TIME_EXCEED.getMessage());

    }

    @Test
    @DisplayName("투표 만료까지 남은 시간 조회 테스트")
    void getPromiseVoteRemainingTime() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(2))
                .build();

        promiseRepository.save(promise);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now().plusDays(2).plusHours(2).plusMinutes(30).plusSeconds(10))
                .build();

        promiseVoteRepository.save(promiseVote);

        em.flush();
        em.clear();

        Promise findPromise = promiseRepository.findById(1L).get();
        PromiseVote findPromiseVote = promiseVoteRepository.findById(1L).get();

        findPromise.setPromiseVote(findPromiseVote);

        em.flush();
        em.clear();

        assertThat(promiseService.getPromiseVoteRemainingTime(1L).getPromiseVoteRemainingTime()).isEqualTo("D-2");
    }

    @Test
    @DisplayName("투표 만료까지 남은 시간 조회 예외 테스트")
    void getPromiseVoteRemainingTimeException() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(2))
                .build();

        promiseRepository.save(promise);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now().minusDays(3))
                .build();

        promiseVoteRepository.save(promiseVote);

        em.flush();
        em.clear();

        Promise findPromise = promiseRepository.findById(1L).get();
        PromiseVote findPromiseVote = promiseVoteRepository.findById(1L).get();

        findPromise.setPromiseVote(findPromiseVote);

        em.flush();
        em.clear();

        assertThatThrownBy(() -> promiseService.getPromiseVoteRemainingTime(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TIME_EXCEED.getMessage());
    }

    @Test
    @DisplayName("투표 후보 목록 조회 테스트")
    void getVoteCandidateList() {
        // given: 사용자 및 약속 생성
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

        em.flush();
        em.clear();

        // given: 약속 멤버 생성
        User findUser = userRepository.findById(1L).get();
        Promise findPromise = promiseRepository.findById(1L).get();

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, "#F4F4F4", findUser, findPromise);
        promiseMemberRepository.save(promiseMember);

        // given: 장소 및 제안된 장소 생성
        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .location(GeometryUtil.createPoint(37.7749295, 122.4194155))
                .placeAddress("광진구 화양동")
                .build();
        placeRepository.save(place);

        em.flush();
        em.clear();

        // given: 투표 데이터 생성
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).orElseThrow();

        PromiseSuggestedPlace promiseSuggestedPlace = PromiseSuggestedPlace.createPromiseSuggestedPlace(findPromiseMember, findPlace);
        promiseSuggestedPlaceRepository.save(promiseSuggestedPlace);

        // given: 약속 투표 생성
        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();
        promiseVoteRepository.save(promiseVote);

        findPromise = promiseRepository.findById(1L).get();
        findPromise.setPromiseVote(promiseVote);
        em.flush();
        em.clear();

        // given: 투표 후보 장소 및 시간 생성
        PromiseVote findPromiseVote = promiseVoteRepository.findById(1L).get();
        findPlace = placeRepository.findById(1L).get();
        PromiseCandidatePlace promiseCandidatePlace = PromiseCandidatePlace.createPromiseCandidatePlace(findPromiseVote, findPlace);
        promiseCandidatePlaceRepository.save(promiseCandidatePlace);

        PromiseCandidateTime promiseCandidateTime = PromiseCandidateTime.createPromiseCandidateTime(
                LocalDate.of(2025, 7, 3), LocalTime.now(), findPromiseVote);
        promiseCandidateTimeRepository.save(promiseCandidateTime);

        em.flush();
        em.clear();

        // when: 사용자가 실제로 투표한 기록 추가
        PromiseCandidateTime findPromiseCandidateTime = promiseCandidateTimeRepository.findById(1L).get();
        PromiseCandidatePlace findPromiseCandidatePlace = promiseCandidatePlaceRepository.findById(1L).get();
        findPromiseVote = promiseVoteRepository.findById(1L).get();
        findPromiseMember = promiseMemberRepository.findById(1L).get();

        PromiseTimeVoteHistory promiseTimeVoteHistory = PromiseTimeVoteHistory.createPromiseTimeVoteHistory(
                findPromiseVote, findPromiseCandidateTime, findPromiseMember);
        promiseTimeVoteHistoryRepository.save(promiseTimeVoteHistory);

        PromisePlaceVoteHistory promisePlaceVoteHistory = PromisePlaceVoteHistory.createPromisePlaceVoteHistory(
                findPromiseCandidatePlace, findPromiseVote, findPromiseMember);
        promisePlaceVoteHistoryRepository.save(promisePlaceVoteHistory);

        em.flush();
        em.clear();

        assertThat(promiseService.getVoteCandidateList(1L, 1L, true).getCandidateTimes().get(0).getIsSelected()).isTrue();
        assertThat(promiseService.getVoteCandidateList(1L, 1L, true).getCandidateTimes().get(0).getDate()).isEqualTo(DateUtil.formatDate(LocalDate.of(2025, 7, 3)));

        assertThat(promiseService.getVoteCandidateList(1L, 1L, true).getCandidatePlaces().get(0).getIsSelected()).isTrue();
        assertThat(promiseService.getVoteCandidateList(1L, 1L, true).getCandidatePlaces().get(0).getPlaceName()).isEqualTo("스타벅스 건대점");

    }


}