package konkuk.kuit.baro.domain.vote.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidateTime;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.promise.repository.PromiseAvailableTimeRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseCandidateTimeRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseMemberRepository;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseTimeVoteHistory;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseTimeVoteHistoryRepositoryTest {

    @Autowired private PromiseTimeVoteHistoryRepository promiseTimeVoteHistoryRepository;
    @Autowired private PromiseRepository promiseRepository;
    @Autowired private PromiseMemberRepository promiseMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PromiseCandidateTimeRepository promiseCandidateTimeRepository;
    @Autowired private PromiseVoteRepository promiseVoteRepository;

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

        promiseRepository.save(promise);

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, "#F4F4F4", user, promise);

        promiseMemberRepository.save(promiseMember);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        PromiseVote savedPromiseVote = promiseVoteRepository.save(promiseVote);

        PromiseCandidateTime promiseCandidateTime = PromiseCandidateTime.createPromiseCandidateTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), savedPromiseVote);

        promiseCandidateTimeRepository.save(promiseCandidateTime);

        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("약속 시간 투표 내역 저장 테스트")
    void save() {
        // given
        PromiseCandidateTime promiseCandidateTime = promiseCandidateTimeRepository.findById(1L).get();
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();

        // when
        PromiseTimeVoteHistory promiseTimeVoteHistory = PromiseTimeVoteHistory.createPromiseTimeVoteHistory(promiseVote, promiseCandidateTime);

        promiseTimeVoteHistoryRepository.save(promiseTimeVoteHistory);

        em.flush();
        em.clear();

        // then
        assertThat(promiseTimeVoteHistoryRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 시간 투표 내역 삭제 테스트")
    void delete() {
        // given
        PromiseCandidateTime promiseCandidateTime = promiseCandidateTimeRepository.findById(1L).get();
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();

        PromiseTimeVoteHistory promiseTimeVoteHistory = PromiseTimeVoteHistory.createPromiseTimeVoteHistory(promiseVote, promiseCandidateTime);

        promiseTimeVoteHistoryRepository.save(promiseTimeVoteHistory);

        em.flush();
        em.clear();

        // when
        PromiseTimeVoteHistory findPromiseTimeVoteHistory = promiseTimeVoteHistoryRepository.findById(1L).get();
        promiseTimeVoteHistoryRepository.delete(findPromiseTimeVoteHistory);

        em.flush();
        em.clear();

        // then
        assertThat(promiseTimeVoteHistoryRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 시간 투표 내역 삭제 테스트")
    @Description("약속을 삭제했을 때, 약속 시간 투표 내역도 삭제되는 지 테스트. 약속 삭제 -> 약속 투표 삭제 -> 약속 후보 시간 삭제 -> 약속 시간 투표 내역 삭제")
    void delete_promise() {
        // given
        PromiseCandidateTime promiseCandidateTime = promiseCandidateTimeRepository.findById(1L).get();
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        Promise promise = promiseRepository.findById(1L).get();

        promise.setPromiseVote(promiseVote);

        PromiseTimeVoteHistory promiseTimeVoteHistory = PromiseTimeVoteHistory.createPromiseTimeVoteHistory(promiseVote, promiseCandidateTime);

        promiseTimeVoteHistoryRepository.save(promiseTimeVoteHistory);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseTimeVoteHistoryRepository.findById(1L).isEmpty()).isTrue();
    }

}