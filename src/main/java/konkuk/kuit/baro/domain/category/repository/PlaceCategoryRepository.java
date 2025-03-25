package konkuk.kuit.baro.domain.category.repository;

import konkuk.kuit.baro.domain.category.model.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {

    @Query("""
            SELECT pc.category.categoryName
            FROM PlaceCategory pc
            WHERE pc.place.id = :placeId
        """)
    List<String> findCategoryNamesByPlaceId(@Param("placeId") Long placeId);
}
