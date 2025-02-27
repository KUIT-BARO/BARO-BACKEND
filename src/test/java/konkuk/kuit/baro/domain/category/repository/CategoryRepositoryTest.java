package konkuk.kuit.baro.domain.category.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import konkuk.kuit.baro.domain.category.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class CategoryRepositoryTest {

    @Autowired private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void save() {
        // given
        Category category = Category.builder()
                .categoryName("비즈니스")
                .build();

        categoryRepository.save(category);

        em.flush();
        em.clear();

        // when
        Category findCategory = categoryRepository.findById(1L).get();

        // then
        assertThat(findCategory.getCategoryName()).isEqualTo("비즈니스");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void delete() {
        // given
        Category category = Category.builder()
                .categoryName("비즈니스")
                .build();

        categoryRepository.save(category);

        em.flush();
        em.clear();

        // when
        Category findCategory = categoryRepository.findById(1L).get();

        categoryRepository.delete(findCategory);
        em.flush();
        em.clear();

        // then
        boolean exists = categoryRepository.findById(1L).isPresent();
        assertThat(exists).isFalse();

    }


}