package konkuk.kuit.baro.domain.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @PersistenceContext private EntityManager em;

    @Test
    @DisplayName("유저 저장 테스트")
    void save() {
        // given
        User user = User.builder()
                .email("hong@konkuk.ac.kr")
                .name("홍길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .build();

        // when
        userRepository.save(user);

        em.flush();
        em.clear();

        // then
        assertThat(userRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void delete() {
        // given
        User user = User.builder()
                .email("hong@konkuk.ac.kr")
                .name("홍길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .color("0XFFFF")
                .build();

        userRepository.save(user);

        em.flush();
        em.clear();

        // when
        User findUser = userRepository.findById(1L).get();
        userRepository.delete(findUser);

        em.flush();
        em.clear();

        // then
        assertThat(userRepository.findById(1L).isEmpty()).isTrue();
    }

}