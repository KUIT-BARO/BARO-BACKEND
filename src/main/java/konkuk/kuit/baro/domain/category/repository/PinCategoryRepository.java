package konkuk.kuit.baro.domain.category.repository;

import konkuk.kuit.baro.domain.category.model.PinCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinCategoryRepository extends JpaRepository<PinCategory, Long> {
}
