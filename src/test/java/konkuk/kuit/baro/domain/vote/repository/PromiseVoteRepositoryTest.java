package konkuk.kuit.baro.domain.vote.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.repository.PromiseRepository;
import konkuk.kuit.baro.domain.vote.model.PromiseVote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseVoteRepositoryTest {

    @Autowired private PromiseVoteRepository promiseVoteRepository;
    @Autowired private PromiseRepository promiseRepository;

    @PersistenceContext private EntityManager em;

    @BeforeEach
    void init() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedPlace("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        // when
        promiseRepository.save(promise);
    }

    @Test
    @DisplayName("약속 투표 저장 테스트")
    void save() {
        // given
        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        // when
        promiseVoteRepository.save(promiseVote);

        em.flush();
        em.clear();

        // then
        assertThat(promiseVoteRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 투표 삭제 테스트")
    void delete() {
        // given
        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        promiseVoteRepository.save(promiseVote);

        em.flush();
        em.clear();

        // when
        PromiseVote findPromiseVote = promiseVoteRepository.findById(1L).get();
        promiseVoteRepository.delete(findPromiseVote);

        em.flush();
        em.clear();

        // then
        assertThat(promiseVoteRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 투표 삭제 테스트")
    @Description("약속이 삭제되었을 때, 약속 투표도 삭제되는지 테스트")
    void delete_promise() {
        // given
        PromiseVote promiseVote = PromiseVote.builder()
                .voteEndTime(LocalDateTime.now())
                .build();

        promiseVoteRepository.save(promiseVote);

        em.flush();
        em.clear();

        Promise findPromise = promiseRepository.findById(1L).get();

        findPromise.setPromiseVote(promiseVote);

        em.flush();
        em.clear();

        // when
        Promise forDeletePromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(forDeletePromise);

        // then
        assertThat(promiseVoteRepository.findById(1L).isEmpty()).isTrue();

    }

}