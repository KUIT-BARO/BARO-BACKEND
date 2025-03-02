package konkuk.kuit.baro.domain.category.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.category.model.Category;
import konkuk.kuit.baro.domain.category.model.PinCategory;
import konkuk.kuit.baro.domain.pin.model.Pin;
import konkuk.kuit.baro.domain.pin.repository.PinRepository;
import konkuk.kuit.baro.domain.place.model.Place;
import konkuk.kuit.baro.domain.place.repository.PlaceRepository;
import konkuk.kuit.baro.domain.user.model.User;
import konkuk.kuit.baro.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PinCategoryRepositoryTest {

    @Autowired private PinCategoryRepository pinCategoryRepository;
    @Autowired private PinRepository pinRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PlaceRepository placeRepository;

    @PersistenceContext private EntityManager em;

    @BeforeEach
    void init() {
        Category category = Category.builder()
                .categoryName("비즈니스")
                .build();

        categoryRepository.save(category);

        User user = User.builder()
                .email("hong@konkuk.ac.kr")
                .name("홍길동")
                .password("qwer1234!")
                .profileImage("image.png")
                .color("0XFFFF")
                .build();

        userRepository.save(user);

        Place place = Place.builder()
                .placeName("스타벅스 건대점")
                .longitude(new BigDecimal("37.7749295"))
                .latitude(new BigDecimal("-122.4194155"))
                .placeAddress("광진구 화양동")
                .build();

        placeRepository.save(place);
    }

    @Test
    @DisplayName("핀 카테고리 저장 테스트")
    @Description("핀을 저장하면 핀 카테고리가 자동으로 저장됨을 확인")
    void save() {
        // given
        User findUser = userRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();
        Category findCategory = categoryRepository.findById(1L).get();

        // when
        Pin pin = Pin.createPin("아늑해요", (short)5, findUser, findPlace);
        PinCategory.createPinCategory(pin, findCategory);
        pinRepository.save(pin);

        em.flush();
        em.clear();

        // then
        assertThat(pinCategoryRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("핀 카테고리 삭제 테스트")
    @Description("핀을 삭제하면 핀 카테고리가 자동으로 삭제됨을 확인")
    void delete() {
        // given
        User findUser = userRepository.findById(1L).get();
        Place findPlace = placeRepository.findById(1L).get();
        Category findCategory = categoryRepository.findById(1L).get();

        Pin pin = Pin.createPin("아늑해요", (short)5, findUser, findPlace);
        PinCategory.createPinCategory(pin, findCategory);
        pinRepository.save(pin);

        em.flush();
        em.clear();

        // when
        Pin findPin = pinRepository.findById(1L).get();
        pinRepository.delete(findPin);

        em.flush();
        em.clear();

        // then
        assertThat(pinCategoryRepository.findById(1L).isPresent()).isFalse();

    }



}