package konkuk.kuit.baro.domain.place.repository;

import konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO;
import konkuk.kuit.baro.domain.place.model.Place;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT DISTINCT p FROM Place p " +
            "JOIN p.placeCategories pc " +
            "WHERE ST_Contains(ST_Buffer(:currentUserLocation, 2000), p.location) " +
            "AND pc.category.id IN :placeCategoryIds")
    List<Place> findByDistanceAndPlaceCategories(
            @Param("currentUserLocation") Point currentUserLocation,
            @Param("placeCategoryIds") List<Long> placeCategoryIds);

    @Query("SELECT p FROM Place p " +
            "WHERE ST_Distance_Sphere(p.location, ST_GeomFromText(:point, 4326)) < 100")
    Optional<Place> findPlaceByLocation(@Param("point") String point);

    @Query(value = """
        SELECT NEW
        konkuk.kuit.baro.domain.place.dto.response.PlaceSummaryInfoResponseDTO(
            p.placeName,
            AVG(pin.score),
            COUNT(pin),
            p.placeAddress
        )
        FROM Place p
        LEFT JOIN p.pins pin
        WHERE p.id = :placeId
        GROUP BY p.id
        """)
    Optional<PlaceSummaryInfoResponseDTO> findPlaceSummaryById(@Param("placeId") Long placeId);

    @Query("SELECT DISTINCT p FROM Place p " +
            "WHERE ST_Contains(ST_Buffer(:suggestedPlaceLocation, 2000), p.location)"
    )
    List<Place> findByLocation(@Param("suggestedPlaceLocation") Point suggestedPlaceLocation);


    @Query("""
    SELECT DISTINCT p
    FROM Place p
    JOIN p.placeCategories pc
    JOIN pc.category c
    WHERE c.categoryName IN :categoryNames AND ST_Contains(ST_Buffer(:suggestedLocation, 2000), p.location)
    """)
    List<Place> findByCategoriesAndDistance(@Param("categoryNames") List<String> categoryNames,
                                            @Param("suggestedLocation") Point suggestedLocation);


}
