package konkuk.kuit.baro.domain.place.repository;

import konkuk.kuit.baro.domain.place.model.Place;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT DISTINCT p FROM Place p " +
            "JOIN p.placeCategories pc " +
            "WHERE ST_Contains(ST_Buffer(:currentUserLocation, 2000), p.location) " +
            "AND pc.category.id IN :placeCategoryIds")
    List<Place> findByDistanceAndPlaceCategories(
            @Param("currentUserLocation") Point currentUserLocation,
            @Param("placeCategoryIds") List<Long> placeCategoryIds);
}
