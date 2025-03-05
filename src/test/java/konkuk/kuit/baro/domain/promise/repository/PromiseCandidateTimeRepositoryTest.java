package konkuk.kuit.baro.domain.promise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseCandidateTime;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import konkuk.kuit.baro.domain.vote.repository.PromiseVoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseCandidateTimeRepositoryTest {

    @Autowired
    private PromiseRepository promiseRepository;
    @Autowired
    private PromiseMemberRepository promiseMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PromiseCandidateTimeRepository promiseCandidateTimeRepository;
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
                .color("0XFFFF")
                .build();

        userRepository.save(user);

        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        Promise savedPromise = promiseRepository.save(promise);

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, user, promise);

        promiseMemberRepository.save(promiseMember);

        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        PromiseVote savedPromiseVote = promiseVoteRepository.save(promiseVote);

        savedPromise.setPromiseVote(savedPromiseVote);

    }

    @Test
    @DisplayName("약속 후보 시간 저장 테스트")
    void save() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        PromiseCandidateTime promiseCandidateTime = PromiseCandidateTime.createPromiseCandidateTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), promiseVote);

        // when
        promiseCandidateTimeRepository.save(promiseCandidateTime);

        // then
        assertThat(promiseCandidateTimeRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 후보 시간 삭제 테스트")
    void delete() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        PromiseCandidateTime promiseCandidateTime = PromiseCandidateTime.createPromiseCandidateTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), promiseVote);

        promiseCandidateTimeRepository.save(promiseCandidateTime);

        em.flush();
        em.clear();

        // when
        PromiseCandidateTime findPromiseCandidateTime = promiseCandidateTimeRepository.findById(1L).get();
        promiseCandidateTimeRepository.delete(findPromiseCandidateTime);

        em.flush();
        em.clear();

        // then
        assertThat(promiseCandidateTimeRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속을 삭제했을 때, 약속 후보 시간도 삭제되는지 테스트. 약속 삭제 -> 약속 투표 삭제 -> 약속 후보 시간 삭제")
    void delete_promise() {
        // given
        PromiseVote promiseVote = promiseVoteRepository.findById(1L).get();
        PromiseCandidateTime promiseCandidateTime = PromiseCandidateTime.createPromiseCandidateTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), promiseVote);

        promiseCandidateTimeRepository.save(promiseCandidateTime);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseCandidateTimeRepository.findById(1L).isEmpty()).isTrue();

    }

}