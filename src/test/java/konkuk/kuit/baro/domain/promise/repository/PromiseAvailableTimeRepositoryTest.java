package konkuk.kuit.baro.domain.promise.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.promise.model.Promise;
import konkuk.kuit.baro.domain.promise.model.PromiseAvailableTime;
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
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PromiseAvailableTimeRepositoryTest {

    @Autowired private PromiseAvailableTimeRepository promiseAvailableTimeRepository;
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
                .color("0XFFFF")
                .build();

        userRepository.save(user);

        Promise promise = Promise.builder()
                .promiseName("컴퓨터 공학부 개강파티")
                .suggestedRegion("지그재그")
                .suggestedStartDate(LocalDate.now().minusDays(1))
                .suggestedEndDate(LocalDate.now().plusDays(1))
                .build();

        promiseRepository.save(promise);

        PromiseMember promiseMember = PromiseMember.createPromiseMember(true, user, promise);

        promiseMemberRepository.save(promiseMember);
    }

    @Test
    @DisplayName("약속 가능 시간 저장 테스트")
    void save() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();

        PromiseAvailableTime promiseAvailableTime = PromiseAvailableTime.createPromiseAvailableTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), findPromiseMember);

        // when
        promiseAvailableTimeRepository.save(promiseAvailableTime);

        em.flush();
        em.clear();

        // then
        assertThat(promiseAvailableTimeRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("약속 가능 시간 삭제 테스트")
    @Description("약속이 삭제되었을 때, 약속 가능 시간도 삭제되는지 테스트. 약속 삭제 -> 약속 참여자 삭제 -> 약속 가능 시간 삭제")
    void delete_promise() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        PromiseAvailableTime promiseAvailableTime = PromiseAvailableTime.createPromiseAvailableTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), findPromiseMember);

        // when
        promiseAvailableTimeRepository.save(promiseAvailableTime);

        em.flush();
        em.clear();

        // when
        Promise findPromise = promiseRepository.findById(1L).get();
        promiseRepository.delete(findPromise);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
        assertThat(promiseAvailableTimeRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("약속 가능 시간 삭제 테스트")
    @Description("유저가 삭제되었을 때, 약속 가능 시간도 삭제되는지 테스트. 유저 삭제 -> 약속 참여자 삭제 -> 약속 가능 시간 삭제")
    void delete_user() {
        // given
        PromiseMember findPromiseMember = promiseMemberRepository.findById(1L).get();
        PromiseAvailableTime promiseAvailableTime = PromiseAvailableTime.createPromiseAvailableTime(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1), findPromiseMember);

        // when
        promiseAvailableTimeRepository.save(promiseAvailableTime);

        em.flush();
        em.clear();

        // when
        User findUser = userRepository.findById(1L).get();
        userRepository.delete(findUser);

        em.flush();
        em.clear();

        // then
        assertThat(promiseMemberRepository.findById(1L).isEmpty()).isTrue();
        assertThat(promiseAvailableTimeRepository.findById(1L).isEmpty()).isTrue();
    }

}