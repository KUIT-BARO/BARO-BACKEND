package konkuk.kuit.baro.domain.promise.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.dto.response.ConfirmedPromiseResponseDTO;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import konkuk.kuit.baro.global.common.exception.CustomException;
import konkuk.kuit.baro.global.common.response.status.ErrorCode;
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
    private PromiseRepository promiseRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired private PromiseService promiseService;
    @Autowired private PromiseVoteRepository promiseVoteRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("약속 현황 - 확정 테스트")
    @Rollback(value = false)
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
        ConfirmedPromiseResponseDTO confirmedPromise = promiseService.getConfirmedPromise(findPromise.getId());

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

        assertThatThrownBy(() -> promiseService.getPromiseVoteRemainingTime(1L).getPromiseVoteRemainingTime())
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TIME_EXCEED.getMessage());
    }

}