package konkuk.kuit.baro.domain.schedule.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.schedule.model.DayOfWeek;
import konkuk.kuit.baro.domain.schedule.model.Schedule;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleRepositoryTest {

    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private UserRepository userRepository;

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
    }

    @Test
    @DisplayName("일정 저장 테스트")
    void save() {
        // given
        User findUser = userRepository.findById(1L).get();

        // when
        Schedule schedule = Schedule.createSchedule("컴퓨터네트워크2", DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1), "공학관 B", findUser);
        scheduleRepository.save(schedule);

        em.flush();
        em.clear();

        // then
        assertThat(scheduleRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    void delete() {
        // given
        User findUser = userRepository.findById(1L).get();
        Schedule schedule = Schedule.createSchedule("컴퓨터네트워크2", DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1), "공학관 B", findUser);
        scheduleRepository.save(schedule);

        em.flush();
        em.clear();

        // when
        Schedule findSchedule = scheduleRepository.findById(1L).get();
        scheduleRepository.delete(findSchedule);

        em.flush();
        em.clear();

        // then
        assertThat(scheduleRepository.findById(1L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    @Description("유저가 삭제되었을 때, 일정도 삭제되는 지 테스트")
    void delete_user() {
        // given
        User findUser = userRepository.findById(1L).get();
        Schedule schedule = Schedule.createSchedule("컴퓨터네트워크2", DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now().plusHours(1), "공학관 B", findUser);
        scheduleRepository.save(schedule);

        em.flush();
        em.clear();

        // when
        User forDeleteUser = userRepository.findById(1L).get();
        userRepository.delete(forDeleteUser);

        em.flush();
        em.clear();

        // then
        assertThat(scheduleRepository.findById(1L).isEmpty()).isTrue();
    }

}