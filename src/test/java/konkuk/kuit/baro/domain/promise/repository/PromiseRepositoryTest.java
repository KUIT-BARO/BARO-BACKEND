package konkuk.kuit.baro.domain.promise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.promise.model.Promise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseRepositoryTest {
    
    @Autowired private PromiseRepository promiseRepository;
    
    @PersistenceContext private EntityManager em;
    
    @Test
    @DisplayName("약속 저장 테스트")
    void save() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        // when
        promiseRepository.save(promise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 삭제 테스트")
    void delete() {
        // given
        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        promiseRepository.save(promise);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();

        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseRepository.findById(1L).isEmpty()).isTrue();
    }

}