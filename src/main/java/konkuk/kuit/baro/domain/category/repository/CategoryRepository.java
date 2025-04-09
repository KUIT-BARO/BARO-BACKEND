package konkuk.kuit.baro.domain.category.repository;

import konkuk.kuit.baro.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCategoryNameIn(List<String> names);
}
