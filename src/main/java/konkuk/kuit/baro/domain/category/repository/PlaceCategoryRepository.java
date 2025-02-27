package konkuk.kuit.baro.domain.category.repository;

import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
}
