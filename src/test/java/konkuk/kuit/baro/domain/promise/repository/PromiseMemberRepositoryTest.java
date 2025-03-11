package konkuk.kuit.baro.domain.promise.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseMember;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseMemberRepositoryTest {

    @Autowired private PromiseMemberRepository promiseMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PromiseRepository promiseRepository;

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
    }

    @Test
    @DisplayName("약속 참여자 저장 테스트")
    void save() {
        // given
        User findUser = userRepository.findById(1L).get();
        Promise findPromise = promiseRepository.findById(1L).get();

        // when
        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, findUser, findPromise);
        promiseMemberRepository.save(promiseMember);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 참여자 삭제 테스트")
    @Description("약속을 삭제했을 때 약속 참여자가 삭제되는지 테스트")
    void delete_promise() {
        // given
        User findUser = userRepository.findById(1L).get();
        Promise findPromise = promiseRepository.findById(1L).get();

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, findUser, findPromise);
        promiseMemberRepository.save(promiseMember);

        em.flush();
        em.clear();

        // when
        Promise forDeletePromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(forDeletePromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 참여자 삭제 테스트")
    @Description("유저를 삭제했을 때 약속 참여자가 삭제되는지 테스트")
    void delete_user() {
        // given
        User findUser = userRepository.findById(1L).get();
        Promise findPromise = promiseRepository.findById(1L).get();

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, findUser, findPromise);
        promiseMemberRepository.save(promiseMember);

        em.flush();
        em.clear();

        // when
        User forDeleteUser = userRepository.findById(1L).get();
        userRepository.delete(forDeleteUser);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
    }

}